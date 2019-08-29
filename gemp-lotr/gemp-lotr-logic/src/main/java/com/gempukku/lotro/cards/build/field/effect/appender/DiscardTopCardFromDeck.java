package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import org.json.simple.JSONObject;

public class DiscardTopCardFromDeck implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "deck", "count", "forced");

        final String deck = FieldUtils.getString(effectObject.get("deck"), "deck", "owner");
        final int count = FieldUtils.getInteger(effectObject.get("count"), "count", 1);
        final boolean forced = FieldUtils.getBoolean(effectObject.get("forced"), "forced");

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(deck, environment);

        return new DelayedAppender() {
            @Override
            public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                final String deckId = playerSource.getPlayer(playerId, game, self, effectResult, effect);

                // Don't check if can discard top cards, since it's a cost
                return game.getGameState().getDeck(deckId).size() >= count
                        && (!forced || game.getModifiersQuerying().canDiscardCardsFromTopOfDeck(game, playerId, self));
            }

            @Override
            protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                final String deckId = playerSource.getPlayer(playerId, game, self, effectResult, effect);

                return new DiscardTopCardFromDeckEffect(self, deckId, count, forced);
            }
        };
    }

}
