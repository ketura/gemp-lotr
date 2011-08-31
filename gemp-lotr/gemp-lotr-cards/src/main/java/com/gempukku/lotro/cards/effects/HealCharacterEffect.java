package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class HealCharacterEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;

    public HealCharacterEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (game.getGameState().getWounds(_physicalCard) > 0);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().removeWound(_physicalCard);
    }
}
