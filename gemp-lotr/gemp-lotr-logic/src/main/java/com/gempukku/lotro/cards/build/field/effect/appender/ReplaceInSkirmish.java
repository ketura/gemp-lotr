package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.DoNothingEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.ReplaceInSkirmishEffect;
import org.json.simple.JSONObject;

public class ReplaceInSkirmish implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "with");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String with = FieldUtils.getString(effectObject.get("with"), "with");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        MultiEffectAppender result = new MultiEffectAppender();
        result.addEffectAppender(
                CardResolver.resolveCard(with, "_temp", "you", "Choose character to replace with in skirmish", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        final LotroPhysicalCard card = actionContext.getCardFromMemory("_temp");
                        if (card != null)
                            return new ReplaceInSkirmishEffect(card, filterableSource.getFilterable(actionContext));
                        else
                            return new DoNothingEffect();
                    }
                });

        return result;
    }

}
