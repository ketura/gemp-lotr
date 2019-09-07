package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
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

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                if (costAppender.isPlayableInFull(actionContext)) {
                    final String preventingPlayer = preventingPlayerSource.getPlayer(actionContext);

                    SubAction subAction = new SubAction(action);
                    subAction.appendEffect(
                            new PlayoutDecisionEffect(preventingPlayer,
                                    new YesNoDecision(text) {
                                        @Override
                                        protected void yes() {
                                            DelegateActionContext delegate = new DelegateActionContext(actionContext,
                                                    preventingPlayer, actionContext.getGame(), actionContext.getSource(), actionContext.getEffectResult(),
                                                    actionContext.getEffect());
                                            costAppender.appendEffect(false, subAction, delegate);
                                            subAction.appendEffect(
                                                    new UnrespondableEffect() {
                                                        @Override
                                                        protected void doPlayEffect(LotroGame game) {
                                                            // If the prevention was not carried out, need to do the original action anyway
                                                            if (!subAction.wasCarriedOut())
                                                                effectAppender.appendEffect(false, subAction, actionContext);
                                                        }

                                                        @Override
                                                        public boolean wasCarriedOut() {
                                                            // Cheating a bit, we need to check, if the preventing effect was carried out,
                                                            // but have no way of doing this, as we can do that through subAction only,
                                                            // and this checking effect should be ALWAYS considered fine, even before it
                                                            // was done
                                                            return true;
                                                        }
                                                    });
                                        }

                                        @Override
                                        protected void no() {
                                            effectAppender.appendEffect(false, subAction, actionContext);
                                        }
                                    }));
                    return new StackActionEffect(subAction);
                } else {
                    SubAction subAction = new SubAction(action);
                    effectAppender.appendEffect(cost, subAction, actionContext);
                    return new StackActionEffect(subAction);
                }
            }

            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                return effectAppender.isPlayableInFull(actionContext);
            }
        };
    }
}
