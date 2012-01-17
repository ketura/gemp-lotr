package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

public class DrawCardsEffect extends AbstractSubActionEffect {
    private Action _action;
    private String _playerId;
    private final int _count;

    public DrawCardsEffect(Action action, String playerId, int count) {
        _action = action;
        _playerId = playerId;
        _count = count;
    }

    @Override
    public String getText(LotroGame game) {
        return "Draw " + _count + " card" + ((_count > 1) ? "s" : "");
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() >= _count;
    }

    @Override
    public void playEffect(LotroGame game) {
        SubAction subAction = new SubAction(_action);
        final List<DrawOneCardEffect> drawEffects = new LinkedList<DrawOneCardEffect>();
        for (int i = 0; i < _count; i++) {
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
