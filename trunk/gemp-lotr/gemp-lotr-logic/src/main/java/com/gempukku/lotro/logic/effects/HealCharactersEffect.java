package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.HealResult;

import java.util.Collection;

public class HealCharactersEffect extends AbstractPreventableCardEffect {
    private String _playerId;

    public HealCharactersEffect(String playerId, PhysicalCard... cards) {
        super(cards);
        _playerId = playerId;
    }

    public HealCharactersEffect(String playerId, Filter filter) {
        super(filter);
        _playerId = playerId;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeHealed(gameState, physicalCard);
            }
        };
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.HEAL;
    }

    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Heal - " + getAppendedNames(cards);
    }

    @Override
    protected EffectResult[] playoutEffectOn(LotroGame game, Collection<PhysicalCard> cards) {
        Collection<PhysicalCard> cardsToHeal = getAffectedCardsMinusPrevented(game);

        for (PhysicalCard cardToHeal : cardsToHeal) {
            game.getGameState().sendMessage(_playerId + " heals " + cardToHeal.getBlueprint().getName());
            game.getGameState().removeWound(cardToHeal);
        }

        return new EffectResult[]{new HealResult(cardsToHeal)};
    }
}
