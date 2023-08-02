package com.gempukku.lotro.cards.build.field.effect.requirement.lotronly;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.requirement.RequirementProducer;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PlayConditions;
import org.json.simple.JSONObject;

public class CanSpotCultureTokens implements RequirementProducer {
    @Override
    public Requirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "culture", "filter", "amount");

        final int count = FieldUtils.getInteger(object.get("amount"), "amount", 1);
        final String filter = FieldUtils.getString(object.get("filter"), "filter", "any");
        final Culture culture = FieldUtils.getEnum(Culture.class, object.get("culture"), "culture");
        final Token tokenForCulture = Token.findTokenForCulture(culture);

        if(tokenForCulture == null)
            throw new InvalidCardDefinitionException("Culture is required for CanSpotCultureTokens.");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return (actionContext) -> PlayConditions.canSpotCultureTokensOnCards(actionContext.getGame(), tokenForCulture, count,
                filterableSource.getFilterable(actionContext));
    }
}
