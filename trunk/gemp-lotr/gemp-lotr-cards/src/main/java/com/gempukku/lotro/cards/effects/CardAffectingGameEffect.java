package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CardAffectingGameEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;

    public CardAffectingGameEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().startAffecting(_physicalCard, game.getModifiersEnvironment());
    }
}
