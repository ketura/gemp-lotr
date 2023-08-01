package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.UnrespondableEffect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MemorizeStacked implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "on", "memory");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "any");
        final String on = FieldUtils.getString(effectObject.get("on"), "on");
        final String memory = FieldUtils.getString(effectObject.get("memory"), "memory");

        final FilterableSource filterSource = environment.getFilterFactory().generateFilter(filter, environment);
        final FilterableSource onFilterSource = environment.getFilterFactory().generateFilter(on, environment);

        return new DelayedAppender<>() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                return new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        final Filterable filterable = filterSource.getFilterable(actionContext);
                        final Filterable onFilterable = onFilterSource.getFilterable(actionContext);
                        final Collection<LotroPhysicalCard> cardsWithStack = Filters.filterActive(game, onFilterable);

                        List<LotroPhysicalCard> cardsToMemorize = new LinkedList<>();
                        for (LotroPhysicalCard cardWithStack : cardsWithStack)
                            cardsToMemorize.addAll(Filters.filter(game.getGameState().getStackedCards(cardWithStack), game, filterable));
                        actionContext.setCardMemory(memory, cardsToMemorize);
                    }
                };
            }
        };
    }

}
