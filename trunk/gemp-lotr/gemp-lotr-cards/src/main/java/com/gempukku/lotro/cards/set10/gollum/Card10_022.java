package com.gempukku.lotro.cards.set10.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

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
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Reclaim the Precious");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, Filters.gollum, Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Gollum", Filters.gollum, Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard gollum) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION, Filters.hasAttached(CardType.ARTIFACT), Filters.canBeAssignedToSkirmishByEffectAgainst(Side.SHADOW, gollum)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard companion) {
                                            action.appendEffect(
                                                    new PreventableEffect(action,
                                                            new AssignmentEffect(playerId, companion, gollum), game.getGameState().getCurrentPlayerId(),
                                                            new PreventableEffect.PreventionCost() {
                                                                @Override
                                                                public Effect createPreventionCostForPlayer(final SubAction subAction, final String playerId) {
                                                                    return new AddBurdenEffect(self, 1) {
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
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
