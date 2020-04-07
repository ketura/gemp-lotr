package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

public class DrawCardsEffect extends AbstractSubActionEffect {
    private Action _action;
    private String _playerId;
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
    public String getText(LotroGame game) {
        final int cardCount = _count.evaluateExpression(game, null);
        return "Draw " + cardCount + " card" + ((cardCount > 1) ? "s" : "");
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() >= _count.evaluateExpression(game, null);
    }

    @Override
    public void playEffect(LotroGame game) {
        SubAction subAction = new SubAction(_action);
        final List<DrawOneCardEffect> drawEffects = new LinkedList<DrawOneCardEffect>();
        final int drawCount = _count.evaluateExpression(game, null);
        for (int i = 0; i < drawCount; i++) {
            final DrawOneCardEffect effect = new DrawOneCardEffect(_playerId);
            subAction.appendEffect(effect);
            drawEffects.add(effect);
        }
        subAction.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
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
