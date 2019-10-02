package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import org.json.simple.JSONObject;

public class AidCost implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "cost");

        final EffectAppender costAppender = environment.getEffectAppenderFactory().getEffectAppender((JSONObject) value.get("cost"), environment);

        blueprint.setAidCostSource(
                new AidCostSource() {
                    @Override
                    public boolean canPayAidCost(DefaultActionContext actionContext) {
                        return costAppender.isPlayableInFull(actionContext);
                    }

                    @Override
                    public void appendAidCost(CostToEffectAction action, DefaultActionContext actionContext) {
                        costAppender.appendEffect(true, action, actionContext);
                    }
                });
    }
}
