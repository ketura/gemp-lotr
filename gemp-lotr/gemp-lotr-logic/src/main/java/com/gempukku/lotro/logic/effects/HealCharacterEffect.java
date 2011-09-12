package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.HealResult;

public class HealCharacterEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;

    public HealCharacterEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    public PhysicalCard getCard() {
        return _physicalCard;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.HEAL;
    }

    @Override
    public String getText() {
        return "Heal " + _physicalCard.getBlueprint().getName();
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (game.getGameState().getWounds(_physicalCard) > 0)
                && game.getModifiersQuerying().canBeHealed(game.getGameState(), _physicalCard);
    }

    @Override
    public EffectResult playEffect(LotroGame game) {
        game.getGameState().removeWound(_physicalCard);
        return new HealResult(_physicalCard);
    }
}
