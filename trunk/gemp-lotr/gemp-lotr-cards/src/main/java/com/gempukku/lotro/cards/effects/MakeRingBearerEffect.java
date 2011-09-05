package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class MakeRingBearerEffect extends UnrespondableEffect {
    private PhysicalCard _newRingBearer;

    public MakeRingBearerEffect(PhysicalCard newRingBearer) {
        _newRingBearer = newRingBearer;
    }

    @Override
    public void playEffect(LotroGame game) {
        if (game.getGameState().isWearingRing())
            game.getGameState().setWearingRing(false);

        PhysicalCard theOneRing = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.THE_ONE_RING));
        game.getGameState().transferCard(theOneRing, _newRingBearer);
        game.getGameState().setRingBearer(_newRingBearer);
    }
}
