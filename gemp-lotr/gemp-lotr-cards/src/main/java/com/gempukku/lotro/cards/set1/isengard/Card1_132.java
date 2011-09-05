package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

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
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Parry", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI));
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose possession", Filters.type(CardType.POSSESSION)) {
                    @Override
                    protected void cardSelected(PhysicalCard possession) {
                        action.addEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new CancelStrengthBonusModifier(self, Filters.sameCard(possession)), Phase.REGROUP));
                    }
                }
        );
        return action;
    }
}
