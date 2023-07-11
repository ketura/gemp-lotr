package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.actions.CostToEffectAction;
import com.gempukku.lotro.game.decisions.YesNoDecision;
import com.gempukku.lotro.game.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.game.timing.Effect;
import org.json.simple.JSONObject;

public class ChooseYesOrNo implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "text", "player", "memorize", "yes", "no");

        final String text = FieldUtils.getString(effectObject.get("text"), "text");
        if (text == null)
            throw new InvalidCardDefinitionException("Text is required for Yes or No decision");
        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize");
        final String yesAnswer = FieldUtils.getString(effectObject.get("yes"), "yes", "yes");
        final String noAnswer = FieldUtils.getString(effectObject.get("no"), "no", "no");
        PlayerSource playerSource = PlayerResolver.resolvePlayer(FieldUtils.getString(effectObject.get("player"), "player", "you"), environment);

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                return new PlayoutDecisionEffect(playerSource.getPlayer(actionContext),
                        new YesNoDecision(GameUtils.SubstituteText(text, actionContext)) {
                            @Override
                            protected void yes() {
                                actionContext.setValueToMemory(memorize, GameUtils.SubstituteText(yesAnswer, actionContext));
                            }

                            @Override
                            protected void no() {
                                actionContext.setValueToMemory(memorize, GameUtils.SubstituteText(noAnswer, actionContext));
                            }
                        });
            }
        };
    }
}
