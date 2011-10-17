package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

public class ChooseAndRemoveTokensFromCardEffect extends ChooseActiveCardEffect {
    private Token _token;
    private int _count;

    public ChooseAndRemoveTokensFromCardEffect(PhysicalCard source, String playerId, Token token, int count, Filterable... filters) {
        super(source, playerId, "Choose card to remove tokens from", filters);
        _token = token;
        _count = count;
    }

    @Override
    protected Filter getExtraFilter() {
        return Filters.hasToken(_token, _count);
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        game.getGameState().removeTokens(card, _token, _count);
    }
}
