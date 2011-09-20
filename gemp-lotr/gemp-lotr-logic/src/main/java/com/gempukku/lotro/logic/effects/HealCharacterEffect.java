package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.HealResult;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HealCharacterEffect extends AbstractEffect {
    private String _playerId;
    private final Filter _filter;

    private Set<PhysicalCard> _healingPrevented = new HashSet<PhysicalCard>();

    public HealCharacterEffect(String playerId, PhysicalCard physicalCard) {
        this(playerId, Filters.sameCard(physicalCard));
    }

    public HealCharacterEffect(String playerId, Filter filter) {
        _playerId = playerId;
        _filter = filter;
    }

    public List<PhysicalCard> getCardsToBeHealed(LotroGame game) {
        List<PhysicalCard> healableCards = new LinkedList<PhysicalCard>();
        for (PhysicalCard physicalCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter)) {
            if (game.getModifiersQuerying().canBeHealed(game.getGameState(), physicalCard))
                healableCards.add(physicalCard);
        }
        healableCards.removeAll(_healingPrevented);

        return healableCards;
    }

    public void preventHeal(PhysicalCard card) {
        _healingPrevented.add(card);
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.HEAL;
    }

    @Override
    public String getText(LotroGame game) {
        List<PhysicalCard> cards = getCardsToBeHealed(game);
        return "Heal - " + getAppendedNames(cards);
    }

    private String getAppendedNames(List<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName() + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 1);
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return getCardsToBeHealed(game).size() > 0;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        List<PhysicalCard> cardsToHeal = getCardsToBeHealed(game);

        for (PhysicalCard cardToHeal : cardsToHeal) {
            game.getGameState().sendMessage(_playerId + " heals " + cardToHeal.getBlueprint().getName());
            game.getGameState().removeWound(cardToHeal);
        }

        return new EffectResult[]{new HealResult(cardsToHeal)};
    }
}
