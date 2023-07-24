package com.gempukku.lotro.game.modifiers.evaluator.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.rules.lotronly.LotroGameUtils;

public class CountFPCulturesEvaluator implements Evaluator {
    private final String _playerId;

    public CountFPCulturesEvaluator(String playerId) {
        _playerId = playerId;
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
        return LotroGameUtils.getSpottableFPCulturesCount(game, _playerId);
    }
}
