package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ReorderTopCardsOfDrawDeck implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count");
        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);

        return new DelayedAppender() {
            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                final Evaluator count = valueSource.getEvaluator(actionContext);
                return actionContext.getGame().getGameState().getDeck(actionContext.getPerformingPlayer()).size() >= count.evaluateExpression(actionContext.getGame(), null);
            }

            @Override
            protected List<? extends Effect> createEffects(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                String deckId = actionContext.getSource().getOwner();

                final int count = valueSource.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                final int deckSize = actionContext.getGame().getGameState().getDeck(deckId).size();
                final int cardCount = Math.min(count, deckSize);

                List<Effect> result = new LinkedList<>();
                result.add(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                List<PhysicalCard> cardsToReorder = new LinkedList<>();
                                cardsToReorder.addAll(game.getGameState().getDeck(deckId).subList(0, cardCount));
                                actionContext.setCardMemory("_temp", cardsToReorder);
                            }
                        });
                for (int i = 0; i < count; i++) {
                    result.add(
                            new UnrespondableEffect() {
                                @Override
                                protected void doPlayEffect(LotroGame game) {
                                    final Collection<? extends PhysicalCard> cards = actionContext.getCardsFromMemory("_temp");
                                    final PlayoutDecisionEffect effect = new PlayoutDecisionEffect(deckId,
                                            new CardsSelectionDecision(1, "Choose card to put on top of deck", cards, 1, 1) {
                                                @Override
                                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                                    final PhysicalCard card = getSelectedCardsByResponse(result).iterator().next();
                                                    game.getGameState().removeCardsFromZone(deckId, Collections.singleton(card));
                                                    game.getGameState().putCardOnTopOfDeck(card);

                                                    cards.remove(card);
                                                }
                                            });
                                    SubAction subAction = new SubAction(action);
                                    subAction.appendEffect(effect);
                                    game.getActionsEnvironment().addActionToStack(subAction);
                                }
                            });
                }

                return result;
            }
        };
    }
}
