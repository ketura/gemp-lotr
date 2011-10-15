package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Spell. Maneuver: Spot Gandalf bearing a staff to prevent a minion from being fierce until the regroup
 * phase.
 */
public class Card2_030 extends AbstractOldEvent {
    public Card2_030() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "You Cannot Pass!", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"), Filters.hasAttached(Filters.possessionClass(PossessionClass.STAFF)));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a minion", Filters.type(CardType.MINION)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new RemoveKeywordModifier(self, Filters.sameCard(card), Keyword.FIERCE), Phase.REGROUP));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
