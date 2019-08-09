package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

public class CountFPCulturesEvaluator implements Evaluator {
    private String _playerId;

    public CountFPCulturesEvaluator(String playerId) {
        _playerId = playerId;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        return GameUtils.getSpottableFPCulturesCount(game, _playerId);
    }
}
