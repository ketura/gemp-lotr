package com.gempukku.lotro.cards.build.field.effect.appender.lotronly;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.MultiEffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.effects.AddTwilightEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.effects.UnrespondableEffect;
import com.gempukku.lotro.game.DefaultGame;
import org.json.simple.JSONObject;

public class AddTwilight implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount", "memorize");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("amount"), 1, environment);
        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        MultiEffectAppender result = new MultiEffectAppender();
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        final int min = valueSource.getMinimum(actionContext);
                        final int max = valueSource.getMaximum(actionContext);
                        if (min != max) {
                            return new PlayoutDecisionEffect(
                                    actionContext.getPerformingPlayer(),
                                    new IntegerAwaitingDecision(1, "Choose how much twilight to add", min, max) {
                                        @Override
                                        public void decisionMade(String result) throws DecisionResultInvalidException {
                                            final int twilight = getValidatedResult(result);
                                            actionContext.setValueToMemory(memorize, String.valueOf(twilight));
                                        }
                                    });
                        } else {
                            return new UnrespondableEffect() {
                                @Override
                                protected void doPlayEffect(DefaultGame game) {
                                    actionContext.setValueToMemory(memorize, String.valueOf(min));
                                }
                            };
                        }
                    }
                });
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        int twilight = Integer.parseInt(actionContext.getValueFromMemory(memorize));
                        return new AddTwilightEffect(actionContext.getSource(), twilight);
                    }
                });
        return result;
    }

}
