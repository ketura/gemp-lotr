package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

public class ReinforceTokenEffect extends ChooseActiveCardEffect {
    private Token _token;
    private int _count;

    public ReinforceTokenEffect(PhysicalCard source, String playerId, Token token) {
        this(source, playerId, token, 1);
    }

    public ReinforceTokenEffect(PhysicalCard source, String playerId, Token token, int count) {
        super(source, playerId, "Choose card to reinforce", Filters.owner(playerId), Filters.hasToken(token));
        _token = token;
        _count = count;
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        game.getGameState().addTokens(card, _token, _count);
    }
}
