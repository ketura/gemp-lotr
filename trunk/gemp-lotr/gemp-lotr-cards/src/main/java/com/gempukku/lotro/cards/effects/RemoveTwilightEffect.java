package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class RemoveTwilightEffect extends AbstractEffect {
    private Evaluator _twilight;

    public RemoveTwilightEffect(int twilight) {
        this(new ConstantEvaluator(twilight));
    }

    public RemoveTwilightEffect(Evaluator twilight) {
        _twilight = twilight;
    }

    @Override
    public String getText(LotroGame game) {
        return "Remove (" + _twilight.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null) + ")";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getTwilightPool() >= _twilight.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int requestedToRemove = _twilight.evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
        int toRemove = Math.min(game.getGameState().getTwilightPool(), requestedToRemove);
        game.getGameState().sendMessage(GameUtils.formatNumber(toRemove, requestedToRemove) + " twilight gets removed from twilight pool");
        game.getGameState().removeTwilight(toRemove);

        return new FullEffectResult(toRemove == requestedToRemove, toRemove == requestedToRemove);
    }
}
