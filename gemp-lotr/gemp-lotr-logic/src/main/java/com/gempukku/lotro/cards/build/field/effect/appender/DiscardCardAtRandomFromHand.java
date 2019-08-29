package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import org.json.simple.JSONObject;

public class DiscardCardAtRandomFromHand implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "forced");

        final boolean forced = FieldUtils.getBoolean(effectObject.get("forced"), "forced");

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                return new DiscardCardAtRandomFromHandEffect(self, self.getOwner(), forced);
            }

            @Override
            public boolean isPlayableInFull(String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                return game.getGameState().getHand(self.getOwner()).size() >= 1
                        && (!forced || game.getModifiersQuerying().canDiscardCardsFromHand(game, playerId, self));
            }
        };
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "forced");

        final boolean forced = FieldUtils.getBoolean(effectObject.get("forced"), "forced");

        return (action, playerId, game, self, effectResult, effect) -> game.getGameState().getHand(self.getOwner()).size() >= 1
                && (!forced || game.getModifiersQuerying().canDiscardCardsFromHand(game, playerId, self));
    }
}
