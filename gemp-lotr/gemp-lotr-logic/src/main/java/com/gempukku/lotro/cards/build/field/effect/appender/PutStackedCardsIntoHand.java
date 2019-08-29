package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PutStackedCardsIntoHand implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "filter", "on");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "choose(any)");
        final String on = FieldUtils.getString(effectObject.get("on"), "on", "any");
        final FilterableSource onFilterSource = environment.getFilterFactory().generateFilter(on);

        MultiEffectAppender result = new MultiEffectAppender();
        result.addEffectAppender(
                CardResolver.resolveStackedCards(filter, valueSource, onFilterSource, "_temp", "owner", "Choose cards to discard", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Iterable<? extends Effect> createEffects(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final Collection<? extends PhysicalCard> cardsToPutToHand = action.getCardsFromMemory("_temp");
                        List<Effect> result = new LinkedList<>();
                        for (PhysicalCard physicalCard : cardsToPutToHand)
                            result.add(new PutCardFromStackedIntoHandEffect(physicalCard));

                        return result;
                    }
                });

        return null;
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "on", "filter", "count");

        String on = FieldUtils.getString(effectObject.get("on"), "on", "any");
        String type = FieldUtils.getString(effectObject.get("filter"), "filter", "choose(any)");
        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);

        final FilterableSource onFilterSource = environment.getFilterFactory().generateFilter(on);

        if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

            return (action, playerId, game, self, effectResult, effect) -> {
                int min = valueSource.getMinimum(action, playerId, game, self, effectResult, effect);
                final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                final Filterable stackedOnFilter = onFilterSource.getFilterable(playerId, game, self, effectResult, effect);

                List<PhysicalCard> choice = new LinkedList<>();

                for (PhysicalCard stackedOn : Filters.filterActive(game, stackedOnFilter)) {
                    final List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(stackedOn);
                    choice.addAll(Filters.filter(stackedCards, game, filterable));
                }

                return choice.size() >= min;
            };
        }
        return null;
    }
}
