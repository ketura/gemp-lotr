package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.actions.Action;

import java.util.LinkedList;
import java.util.List;

public class DrawCardsEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final String _playerId;
    private final Evaluator _count;

    public DrawCardsEffect(Action action, String playerId, Evaluator count) {
        _action = action;
        _playerId = playerId;
        _count = count;
    }

    public DrawCardsEffect(Action action, String playerId, int count) {
        _action = action;
        _playerId = playerId;
        _count = new ConstantEvaluator(count);
    }

    @Override
    public String getText(DefaultGame game) {
        final int cardCount = _count.evaluateExpression(game, null);
        return "Draw " + cardCount + " card" + ((cardCount > 1) ? "s" : "");
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getDeck(_playerId).size() >= _count.evaluateExpression(game, null);
    }

    @Override
    public void playEffect(DefaultGame game) {
        SubAction subAction = new SubAction(_action);
        final List<DrawOneCardEffect> drawEffects = new LinkedList<>();
        final int drawCount = _count.evaluateExpression(game, null);
        for (int i = 0; i < drawCount; i++) {
            final DrawOneCardEffect effect = new DrawOneCardEffect(_playerId);
            subAction.appendEffect(effect);
            drawEffects.add(effect);
        }
        subAction.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        int count = 0;
                        for (DrawOneCardEffect drawEffect : drawEffects) {
                            if (drawEffect.wasCarriedOut())
                                count++;
                        }
                        if (count > 0)
                            game.getGameState().sendMessage(_playerId + " draws " + count + " card" + ((count > 1) ? "s" : ""));
                    }
                }
        );
        processSubAction(game, subAction);
    }
}
