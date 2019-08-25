package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
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
        final String deck = FieldUtils.getString(effectObject.get("deck"), "deck", "owner");
        final int count = FieldUtils.getInteger(effectObject.get("count"), "count", 1);

        MultiEffectAppender result = new MultiEffectAppender();
        String deckPlayerMemory = "_temp1";

        result.addEffectAppender(
                PlayerResolver.resolvePlayer(deck, deckPlayerMemory, environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final String deckId = action.getValueFromMemory(deckPlayerMemory);

                        return new DiscardTopCardFromDeckEffect(self, deckId, count, true);
                    }
                });

        return result;
    }
}
