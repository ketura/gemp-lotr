package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

import java.util.*;

public abstract class AbstractPreventableCardEffect extends AbstractEffect implements PreventableCardEffect {
    private final Filter _filter;
    private final Set<LotroPhysicalCard> _preventedTargets = new HashSet<>();
    private int _requiredTargets;

    public AbstractPreventableCardEffect(LotroPhysicalCard... cards) {
        List<LotroPhysicalCard> affectedCards = Arrays.asList(cards);
        _requiredTargets = affectedCards.size();
        _filter = Filters.in(affectedCards);
    }

    public AbstractPreventableCardEffect(Filterable... filter) {
        _filter = Filters.and(filter);
    }

    protected abstract Filter getExtraAffectableFilter();

    protected final Collection<LotroPhysicalCard> getAffectedCards(DefaultGame game) {
        return Filters.filterActive(game, _filter, getExtraAffectableFilter());
    }

    public final Collection<LotroPhysicalCard> getAffectedCardsMinusPrevented(DefaultGame game) {
        Collection<LotroPhysicalCard> affectedCards = getAffectedCards(game);
        affectedCards.removeAll(_preventedTargets);
        return affectedCards;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return getAffectedCardsMinusPrevented(game).size() >= _requiredTargets;
    }

    protected abstract void playoutEffectOn(DefaultGame game, Collection<LotroPhysicalCard> cards);

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        Collection<LotroPhysicalCard> affectedMinusPreventedCards = getAffectedCardsMinusPrevented(game);
        playoutEffectOn(game, affectedMinusPreventedCards);
        return new FullEffectResult(affectedMinusPreventedCards.size() >= _requiredTargets);
    }

    public void preventEffect(DefaultGame game, LotroPhysicalCard card) {
        _preventedTargets.add(card);
        _prevented = true;
    }
}
