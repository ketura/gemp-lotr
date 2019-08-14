package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CancelStrengthBonusSourceModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Spot an Uruk-hai to cancel the strength bonus from a possession until the regroup phase.
 */
public class Card1_132 extends AbstractEvent {
    public Card1_132() {
        super(Side.SHADOW, 2, Culture.ISENGARD, "Parry", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose possession", CardType.POSSESSION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard possession) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new CancelStrengthBonusSourceModifier(self, Filters.sameCard(possession)), Phase.REGROUP));
                    }
                }
        );
        return action;
    }
}
