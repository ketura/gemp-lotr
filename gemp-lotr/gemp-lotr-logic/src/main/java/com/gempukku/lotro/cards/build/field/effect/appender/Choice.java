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
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Choice implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "effects", "texts");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");
        final JSONObject[] effectArray = FieldUtils.getObjectArray(effectObject.get("effects"), "effects");
        final String[] textArray = FieldUtils.getStringArray(effectObject.get("texts"), "texts");

        if (effectArray.length != textArray.length)
            throw new InvalidCardDefinitionException("Number of texts and effects does not match in choice effect");

        EffectAppender[] possibleEffectAppenders = environment.getEffectAppenderFactory().getEffectAppenders(effectArray, environment);

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);

        return new EffectAppender() {
            @Override
            public void appendCost(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                List<Effect> possibleEffects = new LinkedList<>();
                int textIndex = 0;
                for (EffectAppender possibleEffectAppender : possibleEffectAppenders) {
                    final String text = textArray[textIndex++];
                    possibleEffects.add(
                            new UnrespondableEffect() {
                                @Override
                                protected void doPlayEffect(LotroGame game) {
                                    possibleEffectAppender.appendCost(action, playerId, game, self, effectResult, effect);
                                }

                                @Override
                                public String getText(LotroGame game) {
                                    return text;
                                }

                                @Override
                                public boolean isPlayableInFull(LotroGame game) {
                                    return possibleEffectAppender.isPlayableInFull(action, playerId, game, self, effectResult, effect);
                                }
                            });
                }

                final String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);

                action.appendCost(new ChoiceEffect(action, choicePlayerId, possibleEffects));
            }

            @Override
            public void appendEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                List<Effect> possibleEffects = new LinkedList<>();
                int textIndex = 0;
                for (EffectAppender possibleEffectAppender : possibleEffectAppenders) {
                    final String text = textArray[textIndex++];
                    possibleEffects.add(
                            new UnrespondableEffect() {
                                @Override
                                protected void doPlayEffect(LotroGame game) {
                                    possibleEffectAppender.appendEffect(action, playerId, game, self, effectResult, effect);
                                }

                                @Override
                                public String getText(LotroGame game) {
                                    return text;
                                }

                                @Override
                                public boolean isPlayableInFull(LotroGame game) {
                                    return possibleEffectAppender.isPlayableInFull(action, playerId, game, self, effectResult, effect);
                                }
                            });
                }

                final String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);

                action.appendEffect(new ChoiceEffect(action, choicePlayerId, possibleEffects));
            }

            @Override
            public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                for (EffectAppender possibleEffectAppender : possibleEffectAppenders) {
                    if (possibleEffectAppender.isPlayableInFull(action, playerId, game, self, effectResult, effect))
                        return true;
                }
                return false;
            }
        };
    }
}
