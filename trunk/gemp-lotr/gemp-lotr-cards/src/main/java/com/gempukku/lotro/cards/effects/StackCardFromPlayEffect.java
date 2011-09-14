package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public class StackCardFromPlayEffect extends UnrespondableEffect {
    private PhysicalCard _card;
    private PhysicalCard _stackOn;

    public StackCardFromPlayEffect(PhysicalCard card, PhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return !PlayConditions.nonPlayZone(_card.getZone());
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().stopAffecting(_card);
        game.getGameState().removeCardFromZone(_card);

        GameState gameState = game.getGameState();

        List<PhysicalCard> attachedCards = gameState.getAttachedCards(_card);
        for (PhysicalCard attachedCard : attachedCards) {
            gameState.stopAffecting(attachedCard);
            gameState.removeCardFromZone(attachedCard);
            gameState.addCardToZone(attachedCard, Zone.DISCARD);
        }

        List<PhysicalCard> stackedCards = gameState.getStackedCards(_card);
        for (PhysicalCard stackedCard : stackedCards) {
            gameState.removeCardFromZone(stackedCard);
            gameState.addCardToZone(stackedCard, Zone.DISCARD);
        }

        game.getGameState().sendMessage(_card.getOwner() + " stacks " + _card.getBlueprint().getName() + " from play on " + _stackOn.getBlueprint().getName());
        game.getGameState().stackCard(_card, _stackOn);
    }
}
