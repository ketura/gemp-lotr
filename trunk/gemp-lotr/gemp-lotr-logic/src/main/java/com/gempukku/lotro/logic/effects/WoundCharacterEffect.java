package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collection;

public class WoundCharacterEffect extends AbstractPreventableCardEffect {
    private String _playerId;

    public WoundCharacterEffect(String playerId, PhysicalCard... cards) {
        super(cards);
        _playerId = playerId;
    }

    public WoundCharacterEffect(String playerId, Filter filter) {
        super(filter);
        _playerId = playerId;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canTakeWound(gameState, physicalCard);
            }
        };
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.WOUND;
    }

    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getCardsToBeAffected(game);
        return "Wound - " + getAppendedNames(cards);
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        Collection<PhysicalCard> cardsToWound = getCardsToBeAffected(game);

        for (PhysicalCard woundedCard : cardsToWound) {
            game.getGameState().sendMessage(_playerId + " wounds " + woundedCard.getBlueprint().getName());
            game.getGameState().addWound(woundedCard);
        }

        return new EffectResult[]{new WoundResult(cardsToWound)};
    }
}
