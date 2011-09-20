package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WoundCharacterEffect extends AbstractEffect {
    private String _playerId;
    private final Filter _filter;
    private Set<PhysicalCard> _prevented = new HashSet<PhysicalCard>();

    public WoundCharacterEffect(String playerId, PhysicalCard card) {
        _playerId = playerId;
        _filter = Filters.sameCard(card);
    }

    public WoundCharacterEffect(String playerId, Filter card) {
        _playerId = playerId;
        _filter = card;
    }

    public List<PhysicalCard> getCardsToBeWounded(LotroGame game) {
        List<PhysicalCard> cardsToWound = new LinkedList<PhysicalCard>();
        for (PhysicalCard physicalCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter)) {
            if (game.getModifiersQuerying().canTakeWound(game.getGameState(), physicalCard))
                cardsToWound.add(physicalCard);
        }
        cardsToWound.removeAll(_prevented);
        return cardsToWound;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.WOUND;
    }

    @Override
    public String getText(LotroGame game) {
        List<PhysicalCard> cards = getCardsToBeWounded(game);
        return "Wound - " + getAppendedNames(cards);
    }

    private String getAppendedNames(List<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName() + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return getCardsToBeWounded(game).size() > 0;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        List<PhysicalCard> woundedCards = getCardsToBeWounded(game);

        for (PhysicalCard woundedCard : woundedCards) {
            game.getGameState().sendMessage(_playerId + " wounds " + woundedCard.getBlueprint().getName());
            game.getGameState().addWound(woundedCard);
        }

        return new EffectResult[]{new WoundResult(woundedCards)};
    }

    public void prevent(PhysicalCard card) {
        _prevented.add(card);
    }
}
