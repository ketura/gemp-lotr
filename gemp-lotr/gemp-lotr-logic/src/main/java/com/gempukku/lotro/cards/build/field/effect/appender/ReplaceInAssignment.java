package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.DoNothingEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.ReplaceFpCharacterInAssignmentEffect;
import org.json.simple.JSONObject;

public class ReplaceInAssignment implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "with");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String with = FieldUtils.getString(effectObject.get("with"), "with");

        MultiEffectAppender result = new MultiEffectAppender();
        result.addEffectAppender(
                CardResolver.resolveCard(filter, "_oldAssignee", "you", "Choose assigned character to replace", environment));
        result.addEffectAppender(
                CardResolver.resolveCard(with, "_newAssignee", "you", "Choose character to replace with in assignment", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        final LotroPhysicalCard oldCard = actionContext.getCardFromMemory("_oldAssignee");
                        final LotroPhysicalCard newCard = actionContext.getCardFromMemory("_newAssignee");
                        if (oldCard != null && newCard != null)
                            return new ReplaceFpCharacterInAssignmentEffect(newCard, oldCard);
                        else
                            return new DoNothingEffect();
                    }
                });

        return result;
    }

}
