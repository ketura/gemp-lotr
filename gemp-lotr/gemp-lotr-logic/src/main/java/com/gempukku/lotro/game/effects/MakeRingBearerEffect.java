package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.Collections;

public class MakeRingBearerEffect extends UnrespondableEffect {
    private final LotroPhysicalCard _newRingBearer;

    public MakeRingBearerEffect(LotroPhysicalCard newRingBearer) {
        _newRingBearer = newRingBearer;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        if (game.getGameState().isWearingRing())
            game.getGameState().setWearingRing(false);

        LotroPhysicalCard theOneRing = game.getGameState().getRing(game.getGameState().getCurrentPlayerId());
        game.getGameState().sendMessage(_newRingBearer.getOwner() + " makes " + GameUtils.getCardLink(_newRingBearer) + " a new Ring-bearer");
        game.getGameState().removeCardsFromZone(_newRingBearer.getOwner(), Collections.singleton(theOneRing));
        game.getGameState().attachCard(game, theOneRing, _newRingBearer);
        game.getGameState().setRingBearer(_newRingBearer);
    }
}
