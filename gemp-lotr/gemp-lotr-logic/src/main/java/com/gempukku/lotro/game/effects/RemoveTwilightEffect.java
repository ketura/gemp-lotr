package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;

public class RemoveTwilightEffect extends AbstractEffect {
    private final Evaluator _twilight;

    public RemoveTwilightEffect(int twilight) {
        this(new ConstantEvaluator(twilight));
    }

    public RemoveTwilightEffect(Evaluator twilight) {
        _twilight = twilight;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Remove (" + _twilight.evaluateExpression(game, null) + ")";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getTwilightPool() >= _twilight.evaluateExpression(game, null);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        int requestedToRemove = _twilight.evaluateExpression(game, null);
        int toRemove = Math.min(game.getGameState().getTwilightPool(), requestedToRemove);
        game.getGameState().sendMessage(GameUtils.formatNumber(toRemove, requestedToRemove) + " twilight gets removed from twilight pool");
        game.getGameState().removeTwilight(toRemove);

        return new FullEffectResult(toRemove == requestedToRemove);
    }
}
