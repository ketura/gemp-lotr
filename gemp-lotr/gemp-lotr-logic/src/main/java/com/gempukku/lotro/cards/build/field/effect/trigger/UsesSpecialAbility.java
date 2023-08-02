package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.results.ActivateCardResult;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.TriggerConditions;
import org.json.simple.JSONObject;

public class UsesSpecialAbility implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "memorize");

        String filter = FieldUtils.getString(value.get("filter"), "filter", "any");
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        final String memorize = FieldUtils.getString(value.get("memorize"), "memorize");
        return new TriggerChecker() {
            @Override
            public boolean isBefore() {
                return false;
            }

            @Override
            public boolean accepts(DefaultActionContext<DefaultGame> actionContext) {
                boolean activated = TriggerConditions.activated(actionContext.getGame(), actionContext.getEffectResult(), filterableSource.getFilterable(actionContext));

                if (activated) {
                    ActivateCardResult activateCardResult = (ActivateCardResult) actionContext.getEffectResult();

                    if (memorize != null) {
                        LotroPhysicalCard playedCard = activateCardResult.getSource();
                        actionContext.setCardMemory(memorize, playedCard);
                    }
                }
                return activated;
            }
        };
    }
}
