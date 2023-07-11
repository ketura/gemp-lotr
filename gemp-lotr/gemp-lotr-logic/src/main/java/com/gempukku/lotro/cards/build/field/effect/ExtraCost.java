package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.ExtraPlayCost;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.actions.CostToEffectAction;
import com.gempukku.lotro.game.modifiers.Condition;
import org.json.simple.JSONObject;

public class ExtraCost implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "cost");

        final EffectAppender costAppender = environment.getEffectAppenderFactory().getEffectAppender((JSONObject) value.get("cost"), environment);

        blueprint.appendExtraPlayCost(
                (actionContext) -> new ExtraPlayCost() {
                    @Override
                    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, PhysicalCard card) {
                        costAppender.appendEffect(true, action, actionContext);
                    }

                    @Override
                    public boolean canPayExtraCostsToPlay(DefaultGame game, PhysicalCard card) {
                        return costAppender.isPlayableInFull(actionContext);
                    }

                    @Override
                    public Condition getCondition() {
                        return null;
                    }
                });
    }
}
