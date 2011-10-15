package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Spot an Uruk-hai to cancel the strength bonus from a possession until the regroup phase.
 */
public class Card1_132 extends AbstractOldEvent {
    public Card1_132() {
        super(Side.SHADOW, Culture.ISENGARD, "Parry", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.URUK_HAI));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose possession", Filters.type(CardType.POSSESSION)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard possession) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new CancelStrengthBonusModifier(self, Filters.sameCard(possession)), Phase.REGROUP));
                    }
                }
        );
        return action;
    }
}
