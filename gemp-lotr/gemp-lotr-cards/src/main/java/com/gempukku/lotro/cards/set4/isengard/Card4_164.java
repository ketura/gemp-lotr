package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Assignment: Exert Orthanc Champion to assign it to an unbound companion. That companion
 * may exert to prevent this.
 */
public class Card4_164 extends AbstractMinion {
    public Card4_164() {
        super(5, 12, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Orthanc Champion", null, true);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion, Filters.assignableToSkirmishAgainst(Side.SHADOW, self)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                            action.insertEffect(
                                    new PreventableEffect(action,
                                            new AssignmentEffect(playerId, companion, self),
                                            game.getGameState().getCurrentPlayerId(),
                                            new PreventableEffect.PreventionCost() {
                                                @Override
                                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                    return new ExertCharactersEffect(action, self, companion);
                                                }
                                            }
                                    ));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
