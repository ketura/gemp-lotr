package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

import java.util.List;

public class RevealTopCardsOfDrawDeck implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "deck", "count", "memorize");

        final String deck = FieldUtils.getString(effectObject.get("deck"), "deck", "you");
        final int count = FieldUtils.getInteger(effectObject.get("count"), "count", 1);
        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize");

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(deck, environment);

        return new DelayedAppender() {
            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                final String deckId = playerSource.getPlayer(actionContext);

                return actionContext.getGame().getGameState().getDeck(deckId).size() >= count;
            }

            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                final String deckId = playerSource.getPlayer(actionContext);

                return new RevealTopCardsOfDrawDeckEffect(actionContext.getSource(), deckId, count) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        if (memorize != null)
                            actionContext.setCardMemory(memorize, revealedCards);
                    }
                };
            }
        };
    }
}
