package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

public class AssignToSkirmishAgainstMinion implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "fpCharacter", "against");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");
        final String fpCharacter = FieldUtils.getString(effectObject.get("fpCharacter"), "fpCharacter");
        final String against = FieldUtils.getString(effectObject.get("against"), "against");

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);
        final FilterableSource minionFilter = environment.getFilterFactory().generateFilter(against);

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCard(fpCharacter,
                        (actionContext) -> {
                            final String assigningPlayer = playerSource.getPlayer(actionContext);
                            Side assigningSide = GameUtils.isFP(actionContext.getGame(), assigningPlayer) ? Side.FREE_PEOPLE : Side.SHADOW;
                            final Filterable filter = minionFilter.getFilterable(actionContext);
                            return Filters.assignableToSkirmishAgainst(assigningSide, filter, false, false);
                        }, "_tempFpCharacter", player, "Choose character to assign to minion", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        final String assigningPlayer = playerSource.getPlayer(actionContext);
                        final Filterable filter = minionFilter.getFilterable(actionContext);
                        final PhysicalCard minion = Filters.findFirstActive(actionContext.getGame(), filter);
                        final PhysicalCard fpCharacter = actionContext.getCardFromMemory("_tempFpCharacter");
                        return new AssignmentEffect(assigningPlayer, fpCharacter, minion);
                    }
                });

        return result;
    }

}
