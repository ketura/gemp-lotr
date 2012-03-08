package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ReinforceTokenEffect extends ChooseActiveCardEffect {
    private Token _token;
    private int _count;

    public ReinforceTokenEffect(PhysicalCard source, String playerId, Token token) {
        this(source, playerId, token, 1);
    }

    public ReinforceTokenEffect(PhysicalCard source, String playerId, Token token, int count) {
        super(source, playerId, "Choose card to reinforce", Filters.owner(playerId), (token != null) ? Filters.hasToken(token) : Filters.hasAnyCultureTokens(1),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return !modifiersQuerying.hasFlagActive(gameState, ModifierFlag.CANT_TOUCH_CULTURE_TOKENS);
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
