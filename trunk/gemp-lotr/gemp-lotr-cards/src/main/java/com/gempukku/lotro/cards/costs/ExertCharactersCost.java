package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collection;

public class ExertCharactersCost extends AbstractPreventableCardCost {
    private PhysicalCard _source;

    public ExertCharactersCost(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        _source = source;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeExerted(gameState, _source, physicalCard)
                        && modifiersQuerying.getVitality(gameState, physicalCard) > 1;
            }
        };
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
            game.getGameState().sendMessage(woundedCard.getBlueprint().getName() + " exerts due to " + _source.getBlueprint().getName());
            game.getGameState().addWound(woundedCard);
        }

        return new CostResolution(new EffectResult[]{new ExertResult(woundedCards)}, success);
    }
}
