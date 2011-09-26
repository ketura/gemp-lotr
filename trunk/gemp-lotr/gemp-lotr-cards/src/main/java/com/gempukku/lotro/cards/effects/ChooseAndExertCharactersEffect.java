package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.timing.ChooseableEffect;

import java.util.Collection;

public class ChooseAndExertCharactersEffect extends ChooseActiveCardsEffect implements ChooseableEffect {
    private CostToEffectAction _action;
    private String _playerId;
    private int _minimum;
    private Filter[] _filters;

    public ChooseAndExertCharactersEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose characters to exert", minimum, maximum, Filters.and(filters, Filters.canExert()));
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _filters = filters;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.filter(Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filters), game.getGameState(), game.getModifiersQuerying(), Filters.canExert()).size() >= _minimum;
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> characters) {
        if (_action.getActionSource() != null)
            _action.appendEffect(new CardAffectsCardEffect(_action.getActionSource(), characters));
        _action.appendEffect(new ExertCharacterEffect(_playerId, characters.toArray(new PhysicalCard[characters.size()])));
    }
}
