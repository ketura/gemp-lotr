package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;

public class ReinforceTokenEffect extends ChooseActiveCardEffect {
    private final Token _token;
    private final int _count;

    public ReinforceTokenEffect(PhysicalCard source, String playerId, Token token) {
        this(source, playerId, token, 1);
    }

    public ReinforceTokenEffect(PhysicalCard source, String playerId, Token token, int count) {
        super(source, playerId, "Choose card to reinforce", Filters.owner(playerId), (token != null) ? Filters.hasToken(token) : Filters.hasAnyCultureTokens(1),
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.CANT_TOUCH_CULTURE_TOKENS);
                    }
                });
        _token = token;
        _count = count;
    }

    @Override
    public String getText(LotroGame game) {
        if (_token != null)
            return "Reinforce " + _count + " " + _token.getCulture().getHumanReadable() + " token" + (_count > 1 ? "s" : "");
        else
            return "Reinforce " + _count + " culture token" + (_count > 1 ? "s" : "");
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        if (_token != null)
            game.getGameState().addTokens(card, _token, _count);
        else
            for (Token token : game.getGameState().getTokens(card).keySet())
                if (token.getCulture() != null)
                    game.getGameState().addTokens(card, token, _count);
    }
}
