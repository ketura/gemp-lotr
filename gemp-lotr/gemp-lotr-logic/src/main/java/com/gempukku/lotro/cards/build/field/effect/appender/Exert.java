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
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Exert implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "count", "times", "filter", "memorize");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");
        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final int times = FieldUtils.getInteger(effectObject.get("times"), "times", 1);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter,
                        (playerId, game, source, effectResult, effect) -> Filters.canExert(source, times),
                        valueSource, memory, player, "Choose cards to exert", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Iterable<? extends Effect> createEffects(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final Collection<? extends PhysicalCard> cardsFromMemory = action.getCardsFromMemory(memory);

                        List<Effect> result = new LinkedList<>();
                        for (int i = 0; i < times; i++)
                            result.add(new ExertCharactersEffect(action, self, cardsFromMemory.toArray(new PhysicalCard[0])));
                        return result;
                    }
                });

        return result;
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws
            InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "count", "times", "filter", "memorize");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final int times = FieldUtils.getInteger(effectObject.get("times"), "times", 1);
        final String type = FieldUtils.getString(effectObject.get("filter"), "filter");

        if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

            return (action, playerId, game, self, effectResult, effect) -> {
                final int min = valueSource.getMinimum(null, playerId, game, self, effectResult, effect);
                return PlayConditions.canExert(self, game, times, min,
                        filterableSource.getFilterable(playerId, game, self, effectResult, effect));
            };
        }
        return null;
    }
}
