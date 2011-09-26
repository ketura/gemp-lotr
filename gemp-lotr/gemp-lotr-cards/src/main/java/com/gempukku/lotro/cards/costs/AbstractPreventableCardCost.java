package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableCost;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPreventableCardCost implements ChooseableCost {
    private Set<PhysicalCard> _requiredTargets;
    private Set<PhysicalCard> _preventedTargets = new HashSet<PhysicalCard>();

    public AbstractPreventableCardCost(PhysicalCard... cards) {
        _requiredTargets = new HashSet<PhysicalCard>(Arrays.asList(cards));
    }

    protected abstract Filter getExtraAffectableFilter();

    protected Collection<PhysicalCard> getAffectedCards(LotroGame game) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.in(_requiredTargets), getExtraAffectableFilter());
    }

    public final Collection<PhysicalCard> getCardsToBeAffected(LotroGame game) {
        Collection<PhysicalCard> affectedCards = getAffectedCards(game);
        affectedCards.removeAll(_preventedTargets);
        return affectedCards;
    }

    protected final String getAppendedNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName()).append(", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    @Override
    public boolean canPlayCost(LotroGame game) {
        return getAffectedCards(game).size() == _requiredTargets.size();
    }

    public void preventEffect(PhysicalCard card) {
        _preventedTargets.add(card);
    }

    protected boolean isSuccess(Collection<PhysicalCard> cardsAffected) {
        return _requiredTargets == null || cardsAffected.containsAll(_requiredTargets);
    }
}
