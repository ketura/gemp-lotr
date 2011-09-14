package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

public class ExhaustCharacterEffect extends AbstractEffect {
    private String _playerId;
    private CostToEffectAction _action;
    private boolean _cost;
    private PhysicalCard _physicalCard;
    private boolean _sendMessage;

    public ExhaustCharacterEffect(String playerId, CostToEffectAction action, boolean cost, PhysicalCard physicalCard) {
        this(playerId, action, cost, physicalCard, true);
    }

    private ExhaustCharacterEffect(String playerId, CostToEffectAction action, boolean cost, PhysicalCard physicalCard, boolean sendMessage) {
        _playerId = playerId;
        _action = action;
        _cost = cost;
        _physicalCard = physicalCard;
        _sendMessage = sendMessage;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.EXHAUST;
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
    public EffectResult playEffect(LotroGame game) {
        if (_sendMessage)
            game.getGameState().sendMessage(_playerId + " exhausts " + _physicalCard.getBlueprint().getName());
        game.getGameState().addWound(_physicalCard);
        if (_cost)
            _action.addCost(new ExhaustCharacterEffect(_playerId, _action, _cost, _physicalCard, false));
        else
            _action.addEffect(new ExhaustCharacterEffect(_playerId, _action, _cost, _physicalCard, false));
        return new ExertResult(_physicalCard);
    }
}