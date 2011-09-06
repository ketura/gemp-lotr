package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

public class ExertCharacterEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;
    private boolean _prevented;

    public ExertCharacterEffect(PhysicalCard physicalCard) {
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
        if (!_prevented)
            game.getGameState().addWound(_physicalCard);
    }

    public void prevent() {
        _prevented = true;
    }
}
