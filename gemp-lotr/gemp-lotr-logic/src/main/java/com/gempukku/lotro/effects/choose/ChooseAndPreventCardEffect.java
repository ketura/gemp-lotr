package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.effects.PreventableCardEffect;

public class ChooseAndPreventCardEffect extends ChooseActiveCardEffect {
    private final PreventableCardEffect _effect;

    public ChooseAndPreventCardEffect(LotroPhysicalCard source, PreventableCardEffect effect, String playerId, String choiceText, Filterable... filters) {
        super(source, playerId, choiceText, filters);
        _effect = effect;
    }

    @Override
    protected Filter getExtraFilterForPlaying(DefaultGame game) {
        return Filters.in(_effect.getAffectedCardsMinusPrevented(game));
    }

    @Override
    protected void cardSelected(DefaultGame game, LotroPhysicalCard card) {
        _effect.preventEffect(game, card);
    }
}
