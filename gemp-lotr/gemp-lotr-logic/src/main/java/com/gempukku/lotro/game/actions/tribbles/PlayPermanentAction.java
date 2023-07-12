package com.gempukku.lotro.game.actions.tribbles;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.AbstractCostToEffectAction;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.PlayCardEffect;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.Collections;

public class PlayPermanentAction extends AbstractCostToEffectAction {
    private final PhysicalCard _permanentPlayed;
    private boolean _cardRemoved;
    private Effect _playCardEffect;
    private boolean _cardPlayed;
    private boolean _cardDiscarded;
    private boolean _skipShuffling;
    private final Zone _fromZone;
    private final Zone _toZone;
    private PhysicalCard _playedFromCard;

    public PlayPermanentAction(PhysicalCard card, Zone zone) {
        _permanentPlayed = card;
        setText("Play " + GameUtils.getFullName(_permanentPlayed));
        setPerformingPlayer(card.getOwner());

        if (card.getZone() == Zone.STACKED)
            _playedFromCard = card.getStackedOn();
        else if (card.getZone() == Zone.ATTACHED)
            _playedFromCard = card.getAttachedTo();

        _fromZone = card.getZone();
        _toZone = zone;
    }

    public void skipShufflingDeck() {
        _skipShuffling = true;
    }

    @Override
    public Type getType() {
        return Type.PLAY_CARD;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _permanentPlayed;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _permanentPlayed;
    }

    @Override
    public Effect nextEffect(DefaultGame game) {
        if (!_cardRemoved) {
            _cardRemoved = true;
            final Zone playedFromZone = _permanentPlayed.getZone();
            game.getGameState().sendMessage(_permanentPlayed.getOwner() + " plays " + GameUtils.getCardLink(_permanentPlayed) + " from " + playedFromZone.getHumanReadable());
            game.getGameState().removeCardsFromZone(_permanentPlayed.getOwner(), Collections.singleton(_permanentPlayed));
            if (playedFromZone == Zone.HAND)
                game.getGameState().addCardToZone(game, _permanentPlayed, Zone.VOID_FROM_HAND);
            else
                game.getGameState().addCardToZone(game, _permanentPlayed, Zone.VOID);
            if (playedFromZone == Zone.DECK && !_skipShuffling) {
                game.getGameState().sendMessage(_permanentPlayed.getOwner() + " shuffles their deck");
                game.getGameState().shuffleDeck(_permanentPlayed.getOwner());
            }
        }

        if (!isCostFailed()) {
            Effect cost = getNextCost();
            if (cost != null)
                return cost;

            if (!_cardPlayed) {
                _cardPlayed = true;
                _playCardEffect = new PlayCardEffect(_fromZone, _permanentPlayed, _toZone, _playedFromCard, isPaidToil());
                return _playCardEffect;
            }

            Effect effect = getNextEffect();
            if (effect != null)
                return effect;
        } else {
            if (!_cardDiscarded) {
                _cardDiscarded = true;
                game.getGameState().removeCardsFromZone(_permanentPlayed.getOwner(), Collections.singleton(_permanentPlayed));
                game.getGameState().addCardToZone(game, _permanentPlayed, Zone.DISCARD);
            }
        }

        return null;
    }

    public boolean wasCarriedOut() {
        return _cardPlayed && _playCardEffect != null && _playCardEffect.wasCarriedOut();
    }
}
