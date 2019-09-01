package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.timing.Effect;
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
            public void appendEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                List<Effect> possibleEffects = new LinkedList<>();
                int textIndex = 0;
                for (EffectAppender possibleEffectAppender : possibleEffectAppenders) {
                    final String text = textArray[textIndex++];
                    possibleEffects.add(
                            new UnrespondableEffect() {
                                @Override
                                protected void doPlayEffect(LotroGame game) {
                                    possibleEffectAppender.appendEffect(cost, action, actionContext);
                                }

                                @Override
                                public String getText(LotroGame game) {
                                    return text;
                                }

                                @Override
                                public boolean isPlayableInFull(LotroGame game) {
                                    return possibleEffectAppender.isPlayableInFull(actionContext);
                                }
                            });
                }

                final String choicePlayerId = playerSource.getPlayer(actionContext);

                action.appendEffect(new ChoiceEffect(action, choicePlayerId, possibleEffects));
            }

            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                for (EffectAppender possibleEffectAppender : possibleEffectAppenders) {
                    if (possibleEffectAppender.isPlayableInFull(actionContext))
                        return true;
                }
                return false;
            }
        };
    }
}
