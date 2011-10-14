package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddTokenEffect extends AbstractEffect {
    private PhysicalCard _source;
    private PhysicalCard _target;
    private Token _token;
    private int _count;

    public AddTokenEffect(PhysicalCard source, PhysicalCard target, Token token) {
        this(source, target, token, 1);
    }

    public AddTokenEffect(PhysicalCard source, PhysicalCard target, Token token, int count) {
        _source = source;
        _target = target;
        _token = token;
        _count = count;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _target.getZone().isInPlay();
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().addTokens(_target, _token, _count);

            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
