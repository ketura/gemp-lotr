package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

public class ChooseAndExertCharacterEffect extends ChooseActiveCardEffect {
    private CostToEffectAction _action;
    private String _playerId;
    private boolean _cost;

    public ChooseAndExertCharacterEffect(CostToEffectAction action, String playerId, String choiceText, boolean cost, Filter... filters) {
        super(playerId, choiceText, filters);
        _action = action;
        _playerId = playerId;
        _cost = cost;
    }

    @Override
    protected void cardSelected(PhysicalCard character) {
        if (_cost)
            _action.addCost(new ExertCharacterEffect(_playerId, character));
        else {
            if (_action.getActionSource() != null)
                _action.addEffect(new CardAffectsCardEffect(_action.getActionSource(), character));
            _action.addEffect(new ExertCharacterEffect(_playerId, character));
        }
    }
}
