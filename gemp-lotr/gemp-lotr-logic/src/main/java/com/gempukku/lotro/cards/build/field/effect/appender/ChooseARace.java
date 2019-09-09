package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class ChooseARace implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "memorize");

        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize");

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                final Set<String> possibleRaces = new LinkedHashSet<String>();
                for (Race race : Race.values()) {
                    possibleRaces.add(race.getHumanReadable());
                }

                return new PlayoutDecisionEffect(
                        actionContext.getPerformingPlayer(),
                        new MultipleChoiceAwaitingDecision(1, "Choose a race",
                                possibleRaces.toArray(new String[0])) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                actionContext.setValueToMemory(memorize, Race.findRaceByHumanReadable(result).toString());
                            }
                        });
            }
        };
    }
}
