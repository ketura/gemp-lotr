package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PayPlayOnTwilightCostEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;
    private PhysicalCard _target;
    private int _twilightModifier;

    public PayPlayOnTwilightCostEffect(PhysicalCard physicalCard, PhysicalCard target, int twilightModifier) {
        _physicalCard = physicalCard;
        _target = target;
        _twilightModifier = twilightModifier;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        String currentPlayerId = game.getGameState().getCurrentPlayerId();
        if (currentPlayerId.equals(_physicalCard.getOwner()))
            return true;
        else {
            int twilightCost = _twilightModifier + game.getModifiersQuerying().getPlayOnTwilightCost(game.getGameState(), _physicalCard, _target);

            return twilightCost <= game.getGameState().getTwilightPool();
        }
    }

    @Override
    public void playEffect(LotroGame game) {
        int twilightCost = _twilightModifier + game.getModifiersQuerying().getPlayOnTwilightCost(game.getGameState(), _physicalCard, _target);

        String currentPlayerId = game.getGameState().getCurrentPlayerId();
        if (currentPlayerId.equals(_physicalCard.getOwner()))
            game.getGameState().addTwilight(twilightCost);
        else
            game.getGameState().removeTwilight(twilightCost);
    }
}
