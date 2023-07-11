package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;

public class CountFPCulturesEvaluator implements Evaluator {
    private final String _playerId;

    public CountFPCulturesEvaluator(String playerId) {
        _playerId = playerId;
    }

    @Override
    public int evaluateExpression(DefaultGame game, PhysicalCard cardAffected) {
        return GameUtils.getSpottableFPCulturesCount(game, _playerId);
    }
}
