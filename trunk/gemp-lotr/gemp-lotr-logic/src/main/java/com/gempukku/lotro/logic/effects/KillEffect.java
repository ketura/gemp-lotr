package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

public class KillEffect extends AbstractEffect {
    private PhysicalCard _card;

    public KillEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.KILL;
    }

    public PhysicalCard getKilledCard() {
        return _card;
    }

    @Override
    public String getText() {
        return "Kills character";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        Zone zone = _card.getZone();
        return zone == Zone.FREE_CHARACTERS || zone == Zone.FREE_SUPPORT || zone == Zone.SHADOW_CHARACTERS;
    }

    @Override
    public EffectResult playEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.sendMessage(_card.getBlueprint().getName() + " gets killed");
        gameState.stopAffecting(_card);
        gameState.removeCardFromZone(_card);
        if (_card.getBlueprint().getSide() == Side.FREE_PEOPLE)
            gameState.addCardToZone(_card, Zone.DEAD);
        else
            gameState.addCardToZone(_card, Zone.DISCARD);

        return new KillResult(_card);
    }
}
