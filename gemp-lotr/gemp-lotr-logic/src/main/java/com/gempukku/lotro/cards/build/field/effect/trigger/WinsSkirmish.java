package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import org.json.simple.JSONObject;

public class WinsSkirmish implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "against");

        String winner = FieldUtils.getString(value.get("filter"), "filter", "any");
        String against = FieldUtils.getString(value.get("against"), "against", "any");

        final FilterableSource winnerFilter = environment.getFilterFactory().generateFilter(winner);
        final FilterableSource againstFilter = environment.getFilterFactory().generateFilter(against);

        return new TriggerChecker() {
            @Override
            public boolean accepts(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                return TriggerConditions.winsSkirmishInvolving(game, effectResult,
                        winnerFilter.getFilterable(playerId, game, self, effectResult, effect),
                        againstFilter.getFilterable(playerId, game, self, effectResult, effect));
            }

            @Override
            public boolean isBefore() {
                return false;
            }
        };
    }
}
