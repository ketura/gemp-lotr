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
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
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
                        (playerId, game, source, effectResult, effect) -> {
                            final String assigningPlayer = playerSource.getPlayer(playerId, game, source, effectResult, effect);
                            Side assigningSide = GameUtils.isFP(game, assigningPlayer) ? Side.FREE_PEOPLE : Side.SHADOW;
                            final Filterable filter = minionFilter.getFilterable(playerId, game, source, effectResult, effect);
                            return Filters.assignableToSkirmishAgainst(assigningSide, filter, false, false);
                        }, "_tempFpCharacter", player, "Choose character to assign to minion", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        final String assigningPlayer = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                        final Filterable filter = minionFilter.getFilterable(playerId, game, self, effectResult, effect);
                        final PhysicalCard minion = Filters.findFirstActive(game, filter);
                        final PhysicalCard fpCharacter = action.getCardFromMemory("_tempFpCharacter");
                        return new AssignmentEffect(assigningPlayer, fpCharacter, minion);
                    }
                });

        return result;
    }

    @Override
    public Requirement createCostRequirement(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String player = FieldUtils.getString(effectObject.get("player"), "player", "owner");
        final String type = FieldUtils.getString(effectObject.get("fpCharacter"), "fpCharacter");
        final String against = FieldUtils.getString(effectObject.get("against"), "against");

        final PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);
        final FilterableSource minionFilter = environment.getFilterFactory().generateFilter(against);

        if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

            return (playerId, game, self, effectResult, effect) -> {
                final String assigningPlayer = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                Side assigningSide = GameUtils.isFP(game, assigningPlayer) ? Side.FREE_PEOPLE : Side.SHADOW;

                final Filterable minion = minionFilter.getFilterable(playerId, game, self, effectResult, effect);

                return PlayConditions.isActive(game,
                        filterableSource.getFilterable(playerId, game, self, effectResult, effect),
                        Filters.assignableToSkirmishAgainst(assigningSide, minion, false, false));
            };
        }
        return null;
    }
}
