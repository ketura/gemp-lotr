package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class DiscardCardFromHandEffect implements Effect, Cost {
    private PhysicalCard _card;

    public DiscardCardFromHandEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard from hand - " + _card.getBlueprint().getName();
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        if (_card.getZone() != Zone.HAND)
            return null;

        GameState gameState = game.getGameState();
        gameState.sendMessage(_card.getOwner() + " discards " + _card.getBlueprint().getName() + " from hand");
        gameState.removeCardFromZone(_card);
        gameState.addCardToZone(_card, Zone.DISCARD);

        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        if (_card.getZone() != Zone.HAND)
            return new CostResolution(null, false);

        GameState gameState = game.getGameState();
        gameState.sendMessage(_card.getOwner() + " discards " + _card.getBlueprint().getName() + " from hand");
        gameState.removeCardFromZone(_card);
        gameState.addCardToZone(_card, Zone.DISCARD);

        return new CostResolution(null, true);
    }
}
