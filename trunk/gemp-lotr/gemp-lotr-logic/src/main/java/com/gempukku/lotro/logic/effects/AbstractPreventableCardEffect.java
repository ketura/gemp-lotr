package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableEffect;

import java.util.*;

public abstract class AbstractPreventableCardEffect implements ChooseableEffect {
    private Filter _filter;
    private Set<PhysicalCard> _preventedTargets = new HashSet<PhysicalCard>();

    public AbstractPreventableCardEffect(PhysicalCard... cards) {
        List<PhysicalCard> affectedCards = Arrays.asList(cards);
        _filter = Filters.in(affectedCards);
    }

    public AbstractPreventableCardEffect(Filter filter) {
        _filter = filter;
    }

    protected abstract Filter getExtraAffectableFilter();

    protected final Collection<PhysicalCard> getAffectedCards(LotroGame game) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter, getExtraAffectableFilter());
    }

    protected final String getAppendedNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName() + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    public final Collection<PhysicalCard> getCardsToBeAffected(LotroGame game) {
        Collection<PhysicalCard> affectedCards = getAffectedCards(game);
        affectedCards.removeAll(_preventedTargets);
        return affectedCards;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _filter);
    }

    public void preventEffect(PhysicalCard card) {
        _preventedTargets.add(card);
    }
}
