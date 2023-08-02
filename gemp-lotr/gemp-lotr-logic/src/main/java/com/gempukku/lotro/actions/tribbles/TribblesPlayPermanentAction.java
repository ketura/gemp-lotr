package com.gempukku.lotro.actions.tribbles;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.AbstractCostToEffectAction;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.tribbles.TribblesPlayCardEffect;
import com.gempukku.lotro.rules.GameUtils;

import java.util.Collections;

public class TribblesPlayPermanentAction extends AbstractCostToEffectAction {
    private final LotroPhysicalCard _permanentPlayed;
    private boolean _cardRemoved;

    private Effect _playCardEffect;
    private boolean _cardPlayed;
    private final Zone _fromZone;
    private final Zone _toZone;

    public TribblesPlayPermanentAction(LotroPhysicalCard card, Zone zone) {
        _permanentPlayed = card;
        setText("Play " + GameUtils.getFullName(_permanentPlayed));
        setPerformingPlayer(card.getOwner());

        _fromZone = card.getZone();
        _toZone = zone;
    }

    @Override
    public Type getType() {
        return Type.PLAY_CARD;
    }

    @Override
    public LotroPhysicalCard getActionSource() {
        return _permanentPlayed;
    }

    @Override
    public LotroPhysicalCard getActionAttachedToCard() {
        return _permanentPlayed;
    }

    @Override
    public Effect nextEffect(DefaultGame game) {
        if (!_cardRemoved) {
            _cardRemoved = true;
            final Zone playedFromZone = _permanentPlayed.getZone();
            game.getGameState().sendMessage(_permanentPlayed.getOwner() + " plays " +
                    GameUtils.getCardLink(_permanentPlayed) +  " from " + playedFromZone.getHumanReadable() +
                    " to " + _toZone.getHumanReadable());
            game.getGameState().removeCardsFromZone(_permanentPlayed.getOwner(),
                    Collections.singleton(_permanentPlayed));
            if (playedFromZone == Zone.HAND)
                game.getGameState().addCardToZone(game, _permanentPlayed, Zone.VOID_FROM_HAND);
            else
                game.getGameState().addCardToZone(game, _permanentPlayed, Zone.VOID);
            if (playedFromZone == Zone.DECK) {
                game.getGameState().sendMessage(_permanentPlayed.getOwner() + " shuffles their deck");
                game.getGameState().shuffleDeck(_permanentPlayed.getOwner());
            }
        }

        if (!_cardPlayed) {
            _cardPlayed = true;
            _playCardEffect = new TribblesPlayCardEffect(_fromZone, _permanentPlayed, _toZone);
            return _playCardEffect;
        }

        return getNextEffect();
    }

    public boolean wasCarriedOut() {
        return _cardPlayed && _playCardEffect != null && _playCardEffect.wasCarriedOut();
    }
}
