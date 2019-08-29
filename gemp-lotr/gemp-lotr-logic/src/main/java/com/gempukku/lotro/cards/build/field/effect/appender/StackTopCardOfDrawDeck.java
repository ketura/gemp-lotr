package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import org.json.simple.JSONObject;

public class StackTopCardOfDrawDeck implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "deck", "where", "count");

        final String deck = FieldUtils.getString(effectObject.get("deck"), "deck", "owner");
        final String where = FieldUtils.getString(effectObject.get("where"), "where");
        final int count = FieldUtils.getInteger(effectObject.get("count"), "count", 1);

        MultiEffectAppender result = new MultiEffectAppender();
        String deckPlayerMemory = "_temp1";
        String cardMemory = "_temp2";

        result.addEffectAppender(
                PlayerResolver.resolvePlayer(deck, deckPlayerMemory, environment));
        result.addEffectAppender(
                CardResolver.resolveCard(where, cardMemory, "owner", "Choose card to stack on", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final PhysicalCard card = action.getCardFromMemory(cardMemory);
                        final String deckId = action.getValueFromMemory(deckPlayerMemory);

                        return new StackTopCardsFromDeckEffect(self, deckId, count, card);
                    }
                });

        return result;
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "deck", "where", "count");

        final String deck = FieldUtils.getString(effectObject.get("deck"), "deck", "owner");
        final String type = FieldUtils.getString(effectObject.get("where"), "where");
        final int count = FieldUtils.getInteger(effectObject.get("count"), "count", 1);

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(deck, environment);

        if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

            return (action, playerId, game, self, effectResult, effect) -> {
                String deckId = playerSource.getPlayer(playerId, game, self, effectResult, effect);

                return PlayConditions.canStackDeckTopCards(self, game, deckId, count,
                        filterableSource.getFilterable(playerId, game, self, effectResult, effect));
            };
        } else {
            return (action, playerId, game, self, effectResult, effect) -> {
                String deckId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                return game.getGameState().getDeck(deckId).size() >= count;
            };
        }
    }
}
