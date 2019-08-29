package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import org.json.simple.JSONObject;

public class RemoveBurdens implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "amount");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");
        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);
        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("amount"), 1, environment);

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                final String removingPlayer = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                final Evaluator evaluator = valueSource.getEvaluator(null, playerId, game, self, effectResult, effect);
                return new RemoveBurdenEffect(removingPlayer, self, evaluator.evaluateExpression(game, null));
            }

            @Override
            public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                final Evaluator evaluator = valueSource.getEvaluator(null, playerId, game, self, effectResult, effect);
                final int burdens = evaluator.evaluateExpression(game, null);
                return game.getModifiersQuerying().canRemoveBurden(game, self)
                        && game.getGameState().getBurdens() >= burdens;
            }
        };
    }

}
