package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.results.AddThreatResult;

import java.util.Collections;

public class AddThreatsEffect extends AbstractEffect {
    private Action _action;
    private String _performingPlayer;
    private PhysicalCard _source;
    private int _count;

    public AddThreatsEffect(String performingPlayer, PhysicalCard source, int count) {
        _performingPlayer = performingPlayer;
        _source = source;
        _count = count;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _count <= getThreatsPossibleToAdd(game);
    }

    @Override
    public String getText(LotroGame game) {
        return "Add " + _count + " threat" + ((_count > 1) ? "s" : "");
    }

    @Override
    public Type getType() {
        return null;
    }

    private int getThreatsPossibleToAdd(LotroGame game) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)
                - game.getGameState().getThreats();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int toAdd = Math.min(_count, getThreatsPossibleToAdd(game));
        if (toAdd > 0) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " adds " + toAdd + " threat" + ((toAdd > 1) ? "s" : ""));
            game.getGameState().addThreats(game.getGameState().getCurrentPlayerId(), toAdd);

            return new FullEffectResult(Collections.singleton(new AddThreatResult(_source, toAdd)), toAdd == _count, toAdd == _count);
        }
        return new FullEffectResult(null, false, false);
    }
}
