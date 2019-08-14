package com.gempukku.lotro.cards.set10.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Assignment: Assign Gollum to a companion bearing an artifact. The Free Peoples player may add a burden
 * to prevent this and assign Gollum. Regroup: Exert Gollum twice to discard a possession. Discard this condition.
 */
public class Card10_022 extends AbstractPermanent {
    public Card10_022() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, "Reclaim the Precious");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Gollum", Filters.gollum, Filters.assignableToSkirmishAgainst(Side.SHADOW, Filters.and(CardType.COMPANION, Filters.hasAttached(CardType.ARTIFACT)))) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard gollum) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION, Filters.hasAttached(CardType.ARTIFACT), Filters.assignableToSkirmishAgainst(Side.SHADOW, gollum)) {
                                        @Override
                                        protected void cardSelected(final LotroGame game, PhysicalCard companion) {
                                            action.appendEffect(
                                                    new PreventableEffect(action,
                                                            new AssignmentEffect(playerId, companion, gollum), game.getGameState().getCurrentPlayerId(),
                                                            new PreventableEffect.PreventionCost() {
                                                                @Override
                                                                public Effect createPreventionCostForPlayer(final SubAction subAction, final String playerId) {
                                                                    return new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 1) {
                                                                        @Override
                                                                        public String getText(LotroGame game) {
                                                                            return "Add a burden and assign Gollum";
                                                                        }

                                                                        @Override
                                                                        protected FullEffectResult playEffectReturningResult(LotroGame game) {
                                                                            final FullEffectResult fullEffectResult = super.playEffectReturningResult(game);
                                                                            subAction.appendEffect(
                                                                                    new ChooseAndAssignCharacterToMinionEffect(subAction, playerId, gollum, CardType.COMPANION));
                                                                            return fullEffectResult;
                                                                        }
                                                                    };
                                                                }
                                                            })
                                            );
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game, 2, Filters.gollum)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gollum));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
