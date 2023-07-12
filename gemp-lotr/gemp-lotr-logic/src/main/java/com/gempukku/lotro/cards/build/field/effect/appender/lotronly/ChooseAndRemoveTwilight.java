package com.gempukku.lotro.cards.build.field.effect.appender.lotronly;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.MultiEffectAppender;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.game.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.game.effects.RemoveTwilightEffect;
import com.gempukku.lotro.game.effects.Effect;
import org.json.simple.JSONObject;

public class ChooseAndRemoveTwilight implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "memorize");

        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize");

        MultiEffectAppender result = new MultiEffectAppender();
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        return new PlayoutDecisionEffect(actionContext.getPerformingPlayer(),
                                new IntegerAwaitingDecision(1, "How much twilight do you wish to remove",
                                        0, actionContext.getGame().getGameState().getTwilightPool()) {
                                    @Override
                                    public void decisionMade(String result) throws DecisionResultInvalidException {
                                        actionContext.setValueToMemory(memorize, String.valueOf(getValidatedResult(result)));
                                    }
                                });
                    }
                });
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        int twilight = Integer.parseInt(actionContext.getValueFromMemory(memorize));
                        return new RemoveTwilightEffect(twilight);
                    }
                });

        return result;
    }
}
