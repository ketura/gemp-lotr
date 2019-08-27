package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CountResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.PutCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import org.json.simple.JSONObject;

import java.util.Collection;

public class PutCardFromHandOnBottomOfDeck implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "count", "filter");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");
        final CountResolver.Count count = CountResolver.resolveCount(effectObject.get("count"), 1);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "choose(any)");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCardsInHand(filter, count.getMin(), count.getMax(), "_temp", player, "Choose cards from hand", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final Collection<? extends PhysicalCard> cards = action.getCardsFromMemory("_temp");
                        return new PutCardFromHandOnBottomOfDeckEffect(cards.toArray(new PhysicalCard[0]));
                    }
                });


        return null;
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "player", "filter");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");
        final CountResolver.Count count = CountResolver.resolveCount(effectObject.get("count"), 1);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

        return (playerId, game, self, effectResult, effect) -> {
            final String playerHand = playerSource.getPlayer(playerId, game, self, effectResult, effect);
            final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);

            return Filters.filter(game.getGameState().getHand(playerHand), game, filterable).size() >= count.getMin();
        };
    }
}
