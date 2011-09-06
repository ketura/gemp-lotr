package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class PutCardFromDeckIntoHandOrDiscardEffect extends AbstractEffect {
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
    public EffectResult getRespondableResult() {
        return null;
    }

    @Override
    public String getText() {
        return "Put card from deck into hand";
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().removeCardFromZone(_physicalCard);
        game.getGameState().addCardToZone(_physicalCard, Zone.HAND);
    }
}
