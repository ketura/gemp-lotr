package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

public class WoundCharacterEffect extends AbstractEffect {
    private PhysicalCard _woundTarget;
    private boolean _prevented;

    public WoundCharacterEffect(PhysicalCard woundTarget) {
        _woundTarget = woundTarget;
    }

    @Override
    public EffectResult getRespondableResult() {
        return new WoundResult(_woundTarget);
    }

    @Override
    public String getText() {
        return "Wound target";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        if (!game.getModifiersQuerying().canTakeWound(game.getGameState(), _woundTarget))
            return false;
        Zone zone = _woundTarget.getZone();
        return zone == Zone.FREE_CHARACTERS || zone == Zone.FREE_SUPPORT || zone == Zone.SHADOW_CHARACTERS;
    }

    @Override
    public void playEffect(LotroGame game) {
        if (!_prevented)
            game.getGameState().addWound(_woundTarget);
    }

    public void prevent() {
        _prevented = true;
    }
}
