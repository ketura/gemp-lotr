package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class RemoveTokenEffect extends AbstractEffect {
    private PhysicalCard _source;
    private PhysicalCard _target;
    private Token _token;
    private int _count;

    public RemoveTokenEffect(PhysicalCard source, PhysicalCard target, Token token) {
        this(source, target, token, 1);
    }

    public RemoveTokenEffect(PhysicalCard source, PhysicalCard target, Token token, int count) {
        _source = source;
        _target = target;
        _token = token;
        _count = count;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getTokenCount(_target, _token) >= _count;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int tokenCount = game.getGameState().getTokenCount(_target, _token);
        int removeTokens = Math.min(tokenCount, _count);
        game.getGameState().removeTokens(_target, _token, removeTokens);

        return new FullEffectResult(null, removeTokens == _count, removeTokens == _count);
    }
}
