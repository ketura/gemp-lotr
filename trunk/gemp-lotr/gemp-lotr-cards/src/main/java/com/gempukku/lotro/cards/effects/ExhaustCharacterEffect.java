package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

public class ExhaustCharacterEffect extends AbstractEffect {
    private CostToEffectAction _action;
    private boolean _cost;
    private PhysicalCard _physicalCard;

    public ExhaustCharacterEffect(CostToEffectAction action, boolean cost, PhysicalCard physicalCard) {
        _action = action;
        _cost = cost;
        _physicalCard = physicalCard;
    }

    @Override
    public EffectResult getRespondableResult() {
        return new ExertResult(_physicalCard);
    }

    @Override
    public String getText() {
        return "Exert " + _physicalCard.getBlueprint().getName();
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), _physicalCard);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().addWound(_physicalCard);
        if (_cost)
            _action.addCost(new ExhaustCharacterEffect(_action, _cost, _physicalCard));
        else
            _action.addEffect(new ExhaustCharacterEffect(_action, _cost, _physicalCard));
    }
}