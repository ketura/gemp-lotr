package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.AidCostSource;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppender;
import com.gempukku.lotro.game.DefaultGame;
import org.json.simple.JSONObject;

public class AidCost implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "cost");

        final JSONObject[] costArray = FieldUtils.getObjectArray(value.get("cost"), "cost");

        final EffectAppender[] costAppenders = environment.getEffectAppenderFactory().getEffectAppenders(costArray, environment);

        blueprint.setAidCostSource(
                new AidCostSource() {
                    @Override
                    public boolean canPayAidCost(DefaultActionContext<DefaultGame> actionContext) {
                        for (EffectAppender costAppender : costAppenders) {
                            if (!costAppender.isPlayableInFull(actionContext))
                                return false;
                        }

                        return true;
                    }

                    @Override
                    public void appendAidCost(CostToEffectAction action, DefaultActionContext actionContext) {
                        for (EffectAppender costAppender : costAppenders)
                            costAppender.appendEffect(true, action, actionContext);
                    }
                });
    }
}
