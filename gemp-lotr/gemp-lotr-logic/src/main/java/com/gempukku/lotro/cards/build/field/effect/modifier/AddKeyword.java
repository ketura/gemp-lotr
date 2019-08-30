package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import org.json.simple.JSONObject;

public class AddKeyword implements EffectProcessor {
    @Override
    public void processEffect(JSONObject effectObject, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "condition", "keyword", "amount");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(effectObject.get("condition"), "condition");
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "self");
        final String keywordString = FieldUtils.getString(effectObject.get("keyword"), "keyword");

        final String[] keywordSplit = keywordString.split("\\+");
        Keyword keyword = FieldUtils.getEnum(Keyword.class, keywordSplit[0], "keyword");
        int value = 1;
        if (keywordSplit.length == 2)
            value = Integer.parseInt(keywordSplit[1]);

        final ValueSource amount = ValueResolver.resolveEvaluator(effectObject.get("amount"), value, environment);

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendInPlayModifier(
                new ModifierSource() {
                    @Override
                    public Modifier getModifier(LotroGame game, PhysicalCard self) {
                        final Evaluator evaluator = amount.getEvaluator(null, null, game, self, null, null);
                        return new KeywordModifier(self,
                                filterableSource.getFilterable(null, game, self, null, null),
                                new RequirementCondition(requirements, null, self, null, null), keyword, evaluator);
                    }
                });
    }
}