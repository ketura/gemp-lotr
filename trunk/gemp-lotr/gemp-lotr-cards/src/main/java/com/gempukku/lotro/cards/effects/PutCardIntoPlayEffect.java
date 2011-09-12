package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PutCardIntoPlayEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;
    private Zone _zone;

    public PutCardIntoPlayEffect(PhysicalCard physicalCard, Zone zone) {
        _physicalCard = physicalCard;
        _zone = zone;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.addCardToZone(_physicalCard, _zone);
        gameState.startAffecting(_physicalCard, game.getModifiersEnvironment());
    }
}
