package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PutCardFromDeckOnBottomOfDeckEffect extends UnrespondableEffect {
    private PhysicalCard _source;
    private PhysicalCard _physicalCard;

    public PutCardFromDeckOnBottomOfDeckEffect(PhysicalCard source, PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
        _source = source;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone() == Zone.DECK;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from deck on the bottom of deck");
            gameState.removeCardFromZone(_physicalCard);
            gameState.putCardOnBottomOfDeck(_physicalCard);
        }
    }
}
