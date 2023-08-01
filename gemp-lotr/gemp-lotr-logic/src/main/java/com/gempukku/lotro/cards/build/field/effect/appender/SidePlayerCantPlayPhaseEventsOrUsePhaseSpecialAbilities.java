package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.TimeResolver;
import com.gempukku.lotro.cards.build.field.effect.modifier.RequirementCondition;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.effects.AddUntilModifierEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.modifiers.SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier;
import org.json.simple.JSONObject;

public class SidePlayerCantPlayPhaseEventsOrUsePhaseSpecialAbilities implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "requires", "side", "phase", "until");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(effectObject.get("requires"), "requires");
        final Side side = FieldUtils.getEnum(Side.class, effectObject.get("side"), "side");
        final Phase phase = FieldUtils.getEnum(Phase.class, effectObject.get("phase"), "phase");
        final TimeResolver.Time until = TimeResolver.resolveTime(effectObject.get("until"), "end(current)");

        final Requirement[] conditions = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return new DelayedAppender<>() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                return new AddUntilModifierEffect(
                        new SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(actionContext.getSource(),
                                new RequirementCondition(conditions, actionContext),
                                side, phase), until);
            }
        };
    }
}
