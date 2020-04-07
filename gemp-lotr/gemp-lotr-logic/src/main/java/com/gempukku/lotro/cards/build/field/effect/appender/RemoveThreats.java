package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

public class RemoveThreats implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("amount"), 1, environment);

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                final Evaluator evaluator = valueSource.getEvaluator(null);
                return new RemoveThreatsEffect(actionContext.getSource(), evaluator.evaluateExpression(actionContext.getGame(), null));
            }

            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                final Evaluator evaluator = valueSource.getEvaluator(actionContext);
                final LotroGame game = actionContext.getGame();
                final int threats = evaluator.evaluateExpression(game, null);
                return game.getModifiersQuerying().canRemoveThreat(game, actionContext.getSource())
                        && game.getGameState().getThreats() >= threats;
            }
        };
    }

}
