package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collection;

public class ExertCharactersCost extends AbstractPreventableCardCost {
    private String _playerId;

    public ExertCharactersCost(String playerId, PhysicalCard... cards) {
        super(cards);
        _playerId = playerId;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return Filters.canExert();
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.EXERT;
    }

    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getCardsToBeAffected(game);
        return "Exert - " + getAppendedNames(cards);
    }


    @Override
    public CostResolution playCost(LotroGame game) {
        Collection<PhysicalCard> woundedCards = getCardsToBeAffected(game);

        boolean success = isSuccess(woundedCards);

        for (PhysicalCard woundedCard : woundedCards) {
            game.getGameState().sendMessage(_playerId + " exerts " + woundedCard.getBlueprint().getName());
            game.getGameState().addWound(woundedCard);
        }

        return new CostResolution(new EffectResult[]{new ExertResult(woundedCards)}, success);
    }
}
