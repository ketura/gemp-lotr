package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

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
        return game.getGameState().getTokenCount(_target, _token) >= _count
                && !game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.CANT_TOUCH_CULTURE_TOKENS);
    }

    @Override
    public String getText(LotroGame game) {
        return "Remove " + _count + " " + _token.getCulture().getHumanReadable() + " token" + (_count > 1 ? "s" : "");
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (!game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.CANT_TOUCH_CULTURE_TOKENS)) {
            int tokenCount = game.getGameState().getTokenCount(_target, _token);
            int removeTokens = Math.min(tokenCount, _count);
            if (removeTokens > 0) {
                game.getGameState().removeTokens(_target, _token, removeTokens);
                game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " removed " + removeTokens + " " + _token + " token" + ((removeTokens > 1) ? "s" : "") + " from " + GameUtils.getCardLink(_target));
            }

            return new FullEffectResult(removeTokens == _count);
        } else {
            return new FullEffectResult(false);
        }
    }
}
