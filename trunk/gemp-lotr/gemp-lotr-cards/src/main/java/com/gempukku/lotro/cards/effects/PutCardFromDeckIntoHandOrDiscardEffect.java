package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PutCardFromDeckIntoHandOrDiscardEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;

    public PutCardFromDeckIntoHandOrDiscardEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    public PhysicalCard getCard() {
        return _physicalCard;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return _physicalCard.getZone() == Zone.DECK;
    }

    @Override
    public String getText() {
        return "Put card from deck into hand";
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (game.getModifiersQuerying().canDrawCardAndIncrement(game.getGameState(), _physicalCard.getOwner())) {
            game.getGameState().sendMessage(_physicalCard.getOwner() + " puts card from deck into his or her hand");
            game.getGameState().removeCardFromZone(_physicalCard);
            game.getGameState().addCardToZone(_physicalCard, Zone.HAND);
        } else {
            game.getGameState().sendMessage(_physicalCard.getOwner() + " discards " + _physicalCard.getBlueprint().getName() + " from deck");
            game.getGameState().removeCardFromZone(_physicalCard);
            game.getGameState().addCardToZone(_physicalCard, Zone.DISCARD);
        }
    }
}
