package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;

public class PayTwilightCostEffect implements Cost {
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
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        int twilightCost = _twilightModifier + game.getModifiersQuerying().getTwilightCost(game.getGameState(), _physicalCard);

        String currentPlayerId = game.getGameState().getCurrentPlayerId();
        if (currentPlayerId.equals(_physicalCard.getOwner())) {
            game.getGameState().addTwilight(twilightCost);
            if (twilightCost > 0)
                game.getGameState().sendMessage(_physicalCard.getOwner() + " adds " + twilightCost + " to twilight pool");
            return new CostResolution(null, true);
        } else {
            boolean success = game.getGameState().getTwilightPool() >= twilightCost;
            twilightCost = Math.min(twilightCost, game.getGameState().getTwilightPool());
            if (twilightCost > 0) {
                game.getGameState().removeTwilight(twilightCost);
                game.getGameState().sendMessage(_physicalCard.getOwner() + " removes " + twilightCost + " from twilight pool");
            }
            return new CostResolution(null, success);
        }
    }
}
