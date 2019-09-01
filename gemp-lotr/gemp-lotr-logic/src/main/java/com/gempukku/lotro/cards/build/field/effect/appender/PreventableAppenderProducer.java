package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import org.json.simple.JSONObject;

public class PreventableAppenderProducer implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "text", "player", "effect", "cost");

        final String text = FieldUtils.getString(effectObject.get("text"), "text");
        final String player = FieldUtils.getString(effectObject.get("player"), "player");
        JSONObject effect = (JSONObject) effectObject.get("effect");
        JSONObject cost = (JSONObject) effectObject.get("cost");

        final PlayerSource preventingPlayerSource = PlayerResolver.resolvePlayer(player, environment);
        final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(effect, environment);
        final EffectAppender costAppender = environment.getEffectAppenderFactory().getEffectAppender(cost, environment);

        return new AbstractEffectAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                return new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        if (costAppender.isPlayableInFull(actionContext)) {
                            final String preventingPlayer = preventingPlayerSource.getPlayer(actionContext);
                            action.insertEffect(
                                    new PlayoutDecisionEffect(preventingPlayer,
                                            new YesNoDecision(text) {
                                                @Override
                                                protected void yes() {
                                                    costAppender.appendEffect(cost, action, actionContext);
                                                }

                                                @Override
                                                protected void no() {
                                                    effectAppender.appendEffect(cost, action, actionContext);
                                                }
                                            }));
                        } else {
                            effectAppender.appendEffect(cost, action, actionContext);
                        }
                    }
                };
            }

            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                return effectAppender.isPlayableInFull(actionContext);
            }
        };
    }
}
