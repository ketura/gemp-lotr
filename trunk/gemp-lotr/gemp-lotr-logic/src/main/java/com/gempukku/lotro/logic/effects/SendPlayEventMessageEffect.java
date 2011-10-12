package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class SendPlayEventMessageEffect extends UnrespondableEffect {
    private PhysicalCard _card;

    public SendPlayEventMessageEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        game.getGameState().eventPlayed(_card);
    }
}
