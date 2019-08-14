package com.gempukku.lotro.cards.set40.dwarven;

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
 * Title: Battle Tested
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1U6
 * Game Text: Stack the top 3 cards of your draw deck onto a [DWARVEN] support area condition to make a Dwarf take no more than one wound in a skirmish.
 */
public class Card40_006 extends AbstractEvent{
    public Card40_006() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Battle Tested", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canStackDeckTopCards(self, game, self.getOwner(), 3, Culture.DWARVEN, Zone.SUPPORT, CardType.CONDITION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, self.getOwner(), "Choose condition to stack cards on", Culture.DWARVEN, Zone.SUPPORT, CardType.CONDITION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendCost(new StackTopCardsFromDeckEffect(self, self.getOwner(), 3, card));
                    }
                });
        action.appendEffect(
                new ChooseActiveCardEffect(self, self.getOwner(), "Choose a Dwarf", Race.DWARF) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, card)));
                    }
                }
        );
        return action;
    }
}
