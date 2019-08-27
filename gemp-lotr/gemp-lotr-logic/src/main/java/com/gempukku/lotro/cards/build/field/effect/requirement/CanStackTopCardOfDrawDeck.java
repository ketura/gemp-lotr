package com.gempukku.lotro.cards.build.field.effect.requirement;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.logic.timing.PlayConditions;
import org.json.simple.JSONObject;

public class CanStackTopCardOfDrawDeck implements RequirementProducer {
    @Override
    public PlayRequirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "deck", "where", "count");

        final String deck = FieldUtils.getString(object.get("deck"), "deck", "owner");
        final String where = FieldUtils.getString(object.get("where"), "where");
        final int count = FieldUtils.getInteger(object.get("count"), "count", 1);

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(deck, environment);
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(where);

        return (playerId, game, self, effectResult, effect) -> {
            String deckId = playerSource.getPlayer(playerId, game, self, effectResult, effect);

            return PlayConditions.canStackDeckTopCards(self, game, deckId, count,
                    filterableSource.getFilterable(playerId, game, self, effectResult, effect));
        };
    }
}
