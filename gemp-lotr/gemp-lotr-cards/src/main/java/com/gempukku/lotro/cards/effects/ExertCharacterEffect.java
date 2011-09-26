package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collection;

public class ExertCharacterEffect extends AbstractPreventableCardEffect {
    private String _playerId;

    public ExertCharacterEffect(String playerId, PhysicalCard... cards) {
        super(cards);
        _playerId = playerId;
    }

    public ExertCharacterEffect(String playerId, Filter filter) {
        super(filter);
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
    public EffectResult[] playEffect(LotroGame game) {
        Collection<PhysicalCard> woundedCards = getCardsToBeAffected(game);

        for (PhysicalCard woundedCard : woundedCards) {
            game.getGameState().sendMessage(_playerId + " exerts " + woundedCard.getBlueprint().getName());
            game.getGameState().addWound(woundedCard);
        }

        return new EffectResult[]{new ExertResult(woundedCards)};
    }
}
