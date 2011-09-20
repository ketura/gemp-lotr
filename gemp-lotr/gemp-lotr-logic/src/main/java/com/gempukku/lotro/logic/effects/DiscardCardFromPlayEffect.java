package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.LinkedList;
import java.util.List;

public class DiscardCardFromPlayEffect extends AbstractEffect {
    private PhysicalCard _source;
    private PhysicalCard _card;

    private List<PhysicalCard> _discardedCards;

    public DiscardCardFromPlayEffect(PhysicalCard source, PhysicalCard card) {
        _source = source;
        _card = card;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_PLAY;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard " + _card.getBlueprint().getName() + " from play";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        Zone zone = _card.getZone();
        return zone == Zone.FREE_CHARACTERS || zone == Zone.FREE_SUPPORT || zone == Zone.SHADOW_CHARACTERS || zone == Zone.SHADOW_SUPPORT || zone == Zone.ATTACHED;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        _discardedCards = new LinkedList<PhysicalCard>();

        if (_source == null || game.getModifiersQuerying().canBeDiscardedFromPlay(game.getGameState(), _card, _source)) {
            _discardedCards.add(_card);

            GameState gameState = game.getGameState();
            if (_source != null)
                gameState.sendMessage(_source.getOwner() + " discards " + _card.getBlueprint().getName() + " from play using " + _source.getBlueprint().getName());
            gameState.stopAffecting(_card);
            gameState.removeCardFromZone(_card);
            gameState.addCardToZone(_card, Zone.DISCARD);

            List<PhysicalCard> attachedCards = gameState.getAttachedCards(_card);
            for (PhysicalCard attachedCard : attachedCards) {
                _discardedCards.add(attachedCard);

                gameState.stopAffecting(attachedCard);
                gameState.removeCardFromZone(attachedCard);
                gameState.addCardToZone(attachedCard, Zone.DISCARD);
            }

            List<PhysicalCard> stackedCards = gameState.getStackedCards(_card);
            for (PhysicalCard stackedCard : stackedCards) {
                gameState.removeCardFromZone(stackedCard);
                gameState.addCardToZone(stackedCard, Zone.DISCARD);
            }
        }
        return new EffectResult[]{new DiscardCardsFromPlayResult(_discardedCards)};
    }
}
