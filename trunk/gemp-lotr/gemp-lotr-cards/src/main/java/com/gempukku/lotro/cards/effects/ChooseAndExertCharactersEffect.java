package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.ChooseableEffect;

import java.util.Collection;

public class ChooseAndExertCharactersEffect extends ChooseActiveCardsEffect implements ChooseableEffect {
    private CostToEffectAction _action;
    private String _playerId;

    public ChooseAndExertCharactersEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose characters to exert", minimum, maximum, filters);
        _action = action;
        _playerId = playerId;
    }

    @Override
    protected Filter getExtraFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeExerted(gameState, _action.getActionSource(), physicalCard)
                        && modifiersQuerying.getVitality(gameState, physicalCard) > 1;
            }
        };
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> characters) {
        if (_action.getActionSource() != null)
            _action.appendEffect(new CardAffectsCardEffect(_action.getActionSource(), characters));
        _action.appendEffect(new ExertCharacterEffect(_action.getActionSource(), characters.toArray(new PhysicalCard[characters.size()])));
    }
}
