package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardFromHandResult;

public class DiscardCardFromHandEffect implements Effect, Cost {
    private PhysicalCard _source;
    private PhysicalCard _card;

    public DiscardCardFromHandEffect(PhysicalCard source, PhysicalCard card) {
        _source = source;
        _card = card;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard from hand - " + _card.getBlueprint().getName();
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_HAND;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        if (_card.getZone() != Zone.HAND)
            return null;

        GameState gameState = game.getGameState();
        gameState.sendMessage(_card.getOwner() + " discards " + _card.getBlueprint().getName() + " from hand");
        gameState.removeCardFromZone(_card);
        gameState.addCardToZone(_card, Zone.DISCARD);

        return new EffectResult[]{new DiscardCardFromHandResult(_source, _card)};
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        if (_card.getZone() != Zone.HAND)
            return new CostResolution(null, false);

        GameState gameState = game.getGameState();
        gameState.sendMessage(_card.getOwner() + " discards " + _card.getBlueprint().getName() + " from hand");
        gameState.removeCardFromZone(_card);
        gameState.addCardToZone(_card, Zone.DISCARD);

        return new CostResolution(new EffectResult[]{new DiscardCardFromHandResult(_source, _card)}, true);
    }
}
