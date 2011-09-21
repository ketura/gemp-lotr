package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;

public class ChooseAndHealCharacterEffect extends ChooseActiveCardEffect {
    private CostToEffectAction _action;
    private String _playerId;
    private boolean _cost;

    public ChooseAndHealCharacterEffect(CostToEffectAction action, String playerId, String choiceText, boolean cost, Filter... filters) {
        super(playerId, choiceText, filters);
        _action = action;
        _playerId = playerId;
        _cost = cost;
    }

    @Override
    public String getText(LotroGame game) {
        return "Choose character to heal";
    }

    @Override
    protected void cardSelected(PhysicalCard character) {
        if (_cost) {
            _action.addCost(new HealCharacterEffect(_playerId, character));
        } else {
            if (_action.getActionSource() != null)
                _action.addEffect(new CardAffectsCardEffect(_action.getActionSource(), character));
            _action.addEffect(new HealCharacterEffect(_playerId, character));
        }
    }
}
