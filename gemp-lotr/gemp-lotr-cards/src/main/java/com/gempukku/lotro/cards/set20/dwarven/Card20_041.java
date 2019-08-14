package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * Battle Tested
 * Dwarven	Event • Skirmish
 * Stack the top 3 cards of your draw deck onto a [Dwarven] support area condition to make a Dwarf take no more
 * than one wound in a skirmish.
 */
public class Card20_041 extends AbstractEvent {
    public Card20_041() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Battle Tested", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return game.getGameState().getDeck(self.getOwner()).size() >= 3
                && PlayConditions.canSpot(game, Culture.DWARVEN, Keyword.SUPPORT_AREA, CardType.CONDITION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose condition to stack cards on", Culture.DWARVEN, Keyword.SUPPORT_AREA, CardType.CONDITION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendCost(
                                new StackTopCardsFromDeckEffect(self, playerId, 3, card));
                    }
                });
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf to make him take no more than one wound in a skirmish", Race.DWARF) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, card)));
                    }
                });
        return action;
    }
}
