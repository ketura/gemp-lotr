package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.StackActionEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import org.json.simple.JSONObject;

import java.util.Collection;

public class PlayCardFromStacked implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "on", "removedTwilight");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String onFilter = FieldUtils.getString(effectObject.get("on"), "on");
        final int removedTwilight = FieldUtils.getInteger(effectObject.get("removedTwilight"), "removedTwilight", 0);

        final FilterableSource onFilterableSource = (onFilter != null) ? environment.getFilterFactory().generateFilter(onFilter, environment) : null;

        MultiEffectAppender result = new MultiEffectAppender();
        result.setPlayabilityCheckedForEffect(true);

        result.addEffectAppender(
                CardResolver.resolveStackedCards(filter,
                        actionContext -> Filters.playable(actionContext.getGame()),
                        actionContext -> Filters.playable(actionContext.getGame(), removedTwilight, 0, false, false),
                        new ConstantEvaluator(1), onFilterableSource, "_temp", "you", "Choose card to play", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        final Collection<? extends PhysicalCard> cardsToPlay = actionContext.getCardsFromMemory("_temp");
                        if (cardsToPlay.size() == 1) {
                            final CostToEffectAction playCardAction = PlayUtils.getPlayCardAction(actionContext.getGame(), cardsToPlay.iterator().next(), 0, Filters.any, false);
                            return new StackActionEffect(playCardAction);
                        } else {
                            return new UnrespondableEffect() {
                                @Override
                                protected void doPlayEffect(LotroGame game) {
                                    // do nothing
                                }
                            };
                        }
                    }
                });

        return result;
    }
}
