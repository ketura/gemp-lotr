package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import org.json.simple.JSONObject;

public class ExtraCostToPlay implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "condition", "cost", "filter");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");
        final String filter = FieldUtils.getString(object.get("filter"), "filter");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        final JSONObject[] effectArray = FieldUtils.getObjectArray(object.get("cost"), "cost");
        final EffectAppender[] effectAppenders = environment.getEffectAppenderFactory().getEffectAppenders(effectArray, environment);

        return (actionContext) -> {
            final Filterable filterable = filterableSource.getFilterable(actionContext);
            final RequirementCondition condition = new RequirementCondition(requirements, actionContext);

            return new AbstractExtraPlayCostModifier(actionContext.getSource(), "Cost to play is modified", filterable, condition) {
                @Override
                public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
                    for (EffectAppender effectAppender : effectAppenders)
                        effectAppender.appendEffect(true, action, actionContext);
                }

                @Override
                public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                    for (EffectAppender effectAppender : effectAppenders) {
                        if (!effectAppender.isPlayableInFull(actionContext))
                            return false;
                    }

                    return true;
                }
            };
        };
    }
}
