package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.effects.RemoveBurdenEffect;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.effects.Effect;
import org.json.simple.JSONObject;

public class RemoveBurdens implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "amount");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "you");
        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);
        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("amount"), 1, environment);

        return new DelayedAppender<>() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                final String removingPlayer = playerSource.getPlayer(actionContext);
                final Evaluator evaluator = valueSource.getEvaluator(actionContext);
                return new RemoveBurdenEffect(removingPlayer, actionContext.getSource(), evaluator.evaluateExpression(actionContext.getGame(), null));
            }

            @Override
            public boolean isPlayableInFull(DefaultActionContext<DefaultGame> actionContext) {
                final Evaluator evaluator = valueSource.getEvaluator(actionContext);
                final DefaultGame game = actionContext.getGame();
                final int burdens = evaluator.evaluateExpression(game, null);
                return game.getModifiersQuerying().canRemoveBurden(game, actionContext.getSource())
                        && game.getGameState().getBurdens() >= burdens;
            }
        };
    }

}
