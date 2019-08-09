package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class ForEachThreatEvaluator implements Evaluator {
    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        return game.getGameState().getThreats();
    }
}
