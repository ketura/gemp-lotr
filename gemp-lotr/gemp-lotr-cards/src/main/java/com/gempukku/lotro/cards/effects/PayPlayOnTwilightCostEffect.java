package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;

public class PayPlayOnTwilightCostEffect implements Cost {
    private PhysicalCard _physicalCard;
    private PhysicalCard _target;
    private int _twilightModifier;

    public PayPlayOnTwilightCostEffect(PhysicalCard physicalCard, PhysicalCard target, int twilightModifier) {
        _physicalCard = physicalCard;
        _target = target;
        _twilightModifier = twilightModifier;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        int twilightCost = _twilightModifier + game.getModifiersQuerying().getPlayOnTwilightCost(game.getGameState(), _physicalCard, _target);

        String currentPlayerId = game.getGameState().getCurrentPlayerId();
        if (currentPlayerId.equals(_physicalCard.getOwner())) {
            game.getGameState().addTwilight(twilightCost);
            if (twilightCost > 0)
                game.getGameState().sendMessage(_physicalCard.getOwner() + " adds " + twilightCost + " to twilight pool");
            return new CostResolution(null, true);
        } else {
            int twilightPool = game.getGameState().getTwilightPool();
            boolean success = twilightPool >= twilightCost;
            twilightCost = Math.min(twilightPool, twilightCost);
            game.getGameState().removeTwilight(twilightCost);
            if (twilightCost > 0)
                game.getGameState().sendMessage(_physicalCard.getOwner() + " removes " + twilightCost + " from twilight pool");
            return new CostResolution(null, success);
        }
    }
}
