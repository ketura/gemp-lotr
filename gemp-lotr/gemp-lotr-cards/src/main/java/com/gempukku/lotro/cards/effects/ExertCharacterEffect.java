package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

public class ExertCharacterEffect extends AbstractEffect {
    private String _playerId;
    private PhysicalCard _physicalCard;
    private boolean _prevented;

    public ExertCharacterEffect(String playerId, PhysicalCard physicalCard) {
        _playerId = playerId;
        _physicalCard = physicalCard;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.EXERT;
    }

    public PhysicalCard getExertedCard() {
        return _physicalCard;
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
        if (!_prevented) {
            game.getGameState().sendMessage(_playerId + " exerts " + _physicalCard.getBlueprint().getName());
            game.getGameState().addWound(_physicalCard);
            return new ExertResult(_physicalCard);
        }
        return null;
    }

    public void prevent() {
        _prevented = true;
    }
}
