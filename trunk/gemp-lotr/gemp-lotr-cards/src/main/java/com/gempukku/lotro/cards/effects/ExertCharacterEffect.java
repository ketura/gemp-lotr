package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class ExertCharacterEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;

    public ExertCharacterEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), _physicalCard);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().addWound(_physicalCard);
    }
}
