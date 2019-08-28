package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import org.json.simple.JSONObject;

public class AddBurden implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount", "player");

        final int amount = FieldUtils.getInteger(effectObject.get("amount"), "amount", 1);
        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final String playerAddingBurden = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                        return new AddBurdenEffect(playerAddingBurden, self, amount);
                    }

                    @Override
                    public boolean isPlayableInFull(String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final String playerAddingBurden = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                        return PlayConditions.canAddBurdens(game, playerAddingBurden, self);
                    }
                });

        return result;
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount", "player");

        final String player = FieldUtils.getString(effectObject.get("player"), "player");

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);

        return (playerId, game, self, effectResult, effect) -> {
            final String playerAddingBurden = playerSource.getPlayer(playerId, game, self, effectResult, effect);
            return PlayConditions.canAddBurdens(game, playerAddingBurden, self);
        };
    }
}
