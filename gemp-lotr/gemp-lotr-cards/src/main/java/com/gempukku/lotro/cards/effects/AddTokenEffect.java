package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

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
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            if (_count > 0) {
                game.getGameState().addTokens(_target, _token, _count);
                game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " added " + _count + " " + _token + " token" + ((_count > 1) ? "s" : "") + " to " + GameUtils.getCardLink(_target));
            }

            return new FullEffectResult(true, true);
        }
        return new FullEffectResult(false, false);
    }
}
