package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collection;

public class ExertCharactersEffect extends AbstractPreventableCardEffect {
    private String _playerId;
    private PhysicalCard _source;

    public ExertCharactersEffect(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        _source = source;
    }

    public ExertCharactersEffect(PhysicalCard source, Filter filter) {
        super(filter);
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
        Collection<PhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Exert - " + getAppendedNames(cards);
    }

    @Override
    protected EffectResult[] playoutEffectOn(LotroGame game, Collection<PhysicalCard> cards) {
        for (PhysicalCard woundedCard : cards) {
            game.getGameState().sendMessage(woundedCard.getBlueprint().getName() + " exerts due to " + _source.getBlueprint().getName());
            game.getGameState().addWound(woundedCard);
        }

        return new EffectResult[]{new ExertResult(cards)};
    }
}
