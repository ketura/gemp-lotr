package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

public class WoundCharacterEffect extends AbstractEffect {
    private String _playerId;
    private PhysicalCard _woundTarget;
    private boolean _prevented;

    public WoundCharacterEffect(String playerId, PhysicalCard woundTarget) {
        _playerId = playerId;
        _woundTarget = woundTarget;
    }

    public PhysicalCard getWoundedCard() {
        return _woundTarget;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.WOUND;
    }

    @Override
    public String getText() {
        return "Wound " + _woundTarget.getBlueprint().getName();
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        if (!game.getModifiersQuerying().canTakeWound(game.getGameState(), _woundTarget))
            return false;
        Zone zone = _woundTarget.getZone();
        return zone == Zone.FREE_CHARACTERS || zone == Zone.FREE_SUPPORT || zone == Zone.SHADOW_CHARACTERS;
    }

    @Override
    public EffectResult playEffect(LotroGame game) {
        if (!_prevented) {
            game.getGameState().sendMessage(_playerId + " wounds " + _woundTarget.getBlueprint().getName());
            game.getGameState().addWound(_woundTarget);
        }
        return new WoundResult(_woundTarget);
    }

    public void prevent() {
        _prevented = true;
    }
}
