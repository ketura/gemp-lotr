package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PayTwilightCostEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;
    private int _twilightModifier;

    public PayTwilightCostEffect(PhysicalCard physicalCard) {
        this(physicalCard, 0);
    }

    public PayTwilightCostEffect(PhysicalCard physicalCard, int twilightModifier) {
        _physicalCard = physicalCard;
        _twilightModifier = twilightModifier;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        String currentPlayerId = game.getGameState().getCurrentPlayerId();
        if (currentPlayerId.equals(_physicalCard.getOwner()))
            return true;
        else {
            int twilightCost = _twilightModifier + game.getModifiersQuerying().getTwilightCost(game.getGameState(), _physicalCard);

            return twilightCost <= game.getGameState().getTwilightPool();
        }
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        int twilightCost = _twilightModifier + game.getModifiersQuerying().getTwilightCost(game.getGameState(), _physicalCard);

        String currentPlayerId = game.getGameState().getCurrentPlayerId();
        if (currentPlayerId.equals(_physicalCard.getOwner()))
            game.getGameState().addTwilight(twilightCost);
        else
            game.getGameState().removeTwilight(twilightCost);
    }
}
