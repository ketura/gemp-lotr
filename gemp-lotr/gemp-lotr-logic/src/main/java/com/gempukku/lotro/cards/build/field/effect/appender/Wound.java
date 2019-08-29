package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Wound implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "times", "filter", "memorize");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final int times = FieldUtils.getInteger(effectObject.get("times"), "times", 1);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter,
                        (playerId, game, source, effectResult, effect) -> Filters.canTakeWounds(source, times),
                        valueSource, memory, "owner", "Choose cards to wound", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Iterable<? extends Effect> createEffects(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final Collection<? extends PhysicalCard> cardsFromMemory = action.getCardsFromMemory(memory);
                        List<Effect> result = new LinkedList<>();
                        for (int i = 0; i < times; i++)
                            result.add(new WoundCharactersEffect(self, Filters.in(cardsFromMemory)));
                        return result;
                    }
                });

        return result;
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "times", "filter", "memorize");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final int times = FieldUtils.getInteger(effectObject.get("times"), "times", 1);
        final String type = FieldUtils.getString(effectObject.get("filter"), "filter");

        if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

            return (action, playerId, game, self, effectResult, effect) -> {
                int min = valueSource.getMinimum(action, playerId, game, self, effectResult, effect);
                return PlayConditions.canWound(self, game, times, min,
                        filterableSource.getFilterable(playerId, game, self, effectResult, effect));
            };
        }
        return null;
    }
}
