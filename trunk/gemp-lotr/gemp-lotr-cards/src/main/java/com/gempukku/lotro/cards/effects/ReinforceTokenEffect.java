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
        super(source, playerId, "Choose card to reinforce", Filters.owner(playerId), Filters.hasToken(token),
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
        return "Reinforce " + _count + " " + _token.getCulture().getHumanReadable() + " token" + (_count > 1 ? "s" : "");
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        game.getGameState().addTokens(card, _token, _count);
    }
}
