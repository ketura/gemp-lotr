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
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
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

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                int textIndex = 0;
                List<EffectAppender> playableEffectAppenders = new LinkedList<>();
                List<String> effectTexts = new LinkedList<>();
                for (EffectAppender possibleEffectAppender : possibleEffectAppenders) {
                    if (possibleEffectAppender.isPlayableInFull(actionContext)) {
                        playableEffectAppenders.add(possibleEffectAppender);
                        effectTexts.add(textArray[textIndex]);
                    }
                    textIndex++;
                }

                if (playableEffectAppenders.size() == 0)
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            // Do nothin
                        }
                    };

                if (playableEffectAppenders.size() == 1) {
                    SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                    playableEffectAppenders.get(0).appendEffect(cost, subAction, actionContext);
                    return new StackActionEffect(subAction);
                }

                final String choosingPlayer = playerSource.getPlayer(actionContext);
                SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                subAction.appendCost(
                        new PlayoutDecisionEffect(choosingPlayer,
                                new MultipleChoiceAwaitingDecision(1, "Choose action to perform", effectTexts.toArray(new String[0])) {
                                    @Override
                                    protected void validDecisionMade(int index, String result) {
                                        playableEffectAppenders.get(index).appendEffect(cost, subAction, actionContext);
                                    }
                                }));
                return new StackActionEffect(subAction);
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
