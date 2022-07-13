package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChooseAndHeal implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "optional", "filter", "player");

        final ValueSource count = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final boolean optional = FieldUtils.getBoolean(effectObject.get("optional"), "optional", false);
        final int min = optional ? 0 : 1;
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        final String player = FieldUtils.getString(effectObject.get("player"), "player", "you");
        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);

        //Might implement this later
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        DelayedAppender result = new DelayedAppender() {
            @Override
            protected List<? extends Effect> createEffects(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                final Collection<? extends PhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(memory);
                List<Effect> result = new LinkedList<>();
                final int healCount = count.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                for (int i = 0; i < healCount; i++) {
                    final int remainingHeals = healCount - i;
                    //If the healing is optional (like sanctuary healing, an implied "up to" the maximum), then the minimum
                    // is set to 0, otherwise 1 (as in, the player /must/ choose a character to heal).
                    ChooseAndHealCharactersEffect healEffect = new ChooseAndHealCharactersEffect(action, playerSource.getPlayer(actionContext),
                        min, 1, filterableSource.getFilterable(actionContext));
                    healEffect.setChoiceText("Choose card to heal - remaining heals: " + remainingHeals);
                    result.add(healEffect);
                }

                return result;
            }
        };

        return result;
    }

}
