package com.gempukku.lotro.logic.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableCardEffect;

public class ChooseAndPreventCardEffect extends ChooseActiveCardEffect {
    private final PreventableCardEffect _effect;

    public ChooseAndPreventCardEffect(PhysicalCard source, PreventableCardEffect effect, String playerId, String choiceText, Filterable... filters) {
        super(source, playerId, choiceText, filters);
        _effect = effect;
    }

    @Override
    protected Filter getExtraFilterForPlaying(LotroGame game) {
        return Filters.in(_effect.getAffectedCardsMinusPrevented(game));
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        _effect.preventEffect(game, card);
    }
}
