package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class TransferPermanentEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;
    private PhysicalCard _targetCard;

    public TransferPermanentEffect(PhysicalCard physicalCard, PhysicalCard targetCard) {
        _physicalCard = physicalCard;
        _targetCard = targetCard;
    }

    @Override
    public void playEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.transferCard(_physicalCard, _targetCard);
    }
}
