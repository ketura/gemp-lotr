package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.modifiers.ModifierFlag;

public class AddTokenEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final LotroPhysicalCard _target;
    private final Token _token;
    private final int _count;

    public AddTokenEffect(LotroPhysicalCard source, LotroPhysicalCard target, Token token) {
        this(source, target, token, 1);
    }

    public AddTokenEffect(LotroPhysicalCard source, LotroPhysicalCard target, Token token, int count) {
        _source = source;
        _target = target;
        _token = token;
        _count = count;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _target.getZone().isInPlay() && !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.CANT_TOUCH_CULTURE_TOKENS);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        if (_token != null)
            return "Add " + _count + " " + _token.getCulture().getHumanReadable() + " token" + (_count > 1 ? "s" : "");
        else
            return "Add " + _count + " culture token" + (_count > 1 ? "s" : "");
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            if (_count > 0) {
                game.getGameState().addTokens(_target, _token, _count);
                game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " added " + _count + " " + _token + " token" + ((_count > 1) ? "s" : "") + " to " + GameUtils.getCardLink(_target));
            }

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
