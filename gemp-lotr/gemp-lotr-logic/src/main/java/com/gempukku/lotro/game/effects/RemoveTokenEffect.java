package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.modifiers.ModifierFlag;

public class RemoveTokenEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final PhysicalCard _target;
    private final Token _token;
    private final int _count;

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
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getTokenCount(_target, _token) >= _count
                && !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.CANT_TOUCH_CULTURE_TOKENS);
    }

    @Override
    public String getText(DefaultGame game) {
        return "Remove " + _count + " " + _token.getCulture().getHumanReadable() + " token" + (_count > 1 ? "s" : "");
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (!game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.CANT_TOUCH_CULTURE_TOKENS)) {
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
