package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import org.json.simple.JSONObject;

public class ConditionEffect implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "condition", "effect");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(effectObject.get("condition"), "condition");
        final JSONObject effectJson = (JSONObject) effectObject.get("effect");

        final Requirement[] conditions = environment.getRequirementFactory().getRequirements(conditionArray, environment);
        final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(effectJson, environment);

        return new EffectAppender() {
            @Override
            public void appendCost(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                action.appendCost(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                for (Requirement condition : conditions) {
                                    if (!condition.accepts(action, playerId, game, self, effectResult, effect))
                                        return;
                                }
                                effectAppender.appendCost(action, playerId, game, self, effectResult, effect);
                            }
                        });
            }

            @Override
            public void appendEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                action.appendCost(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                for (Requirement condition : conditions) {
                                    if (!condition.accepts(action, playerId, game, self, effectResult, effect))
                                        return;
                                }
                                effectAppender.appendEffect(action, playerId, game, self, effectResult, effect);
                            }
                        });
            }

            // TODO, maybe check the requirements, and if met, check if the effect is playable?
            @Override
            public boolean isPlayableInFull(ActionContext actionContext, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                return true;
            }
        };
    }

}
