package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

import java.util.Collections;

public class MakeRingBearerEffect extends UnrespondableEffect {
    private final PhysicalCard _newRingBearer;

    public MakeRingBearerEffect(PhysicalCard newRingBearer) {
        _newRingBearer = newRingBearer;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        if (game.getGameState().isWearingRing())
            game.getGameState().setWearingRing(false);

        PhysicalCard theOneRing = game.getGameState().getRing(game.getGameState().getCurrentPlayerId());
        game.getGameState().sendMessage(_newRingBearer.getOwner() + " makes " + GameUtils.getCardLink(_newRingBearer) + " a new Ring-bearer");
        game.getGameState().removeCardsFromZone(_newRingBearer.getOwner(), Collections.singleton(theOneRing));
        game.getGameState().attachCard(game, theOneRing, _newRingBearer);
        game.getGameState().setRingBearer(_newRingBearer);
    }
}
