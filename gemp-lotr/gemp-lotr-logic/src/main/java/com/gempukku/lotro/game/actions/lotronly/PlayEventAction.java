package com.gempukku.lotro.game.actions.lotronly;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.AbstractCostToEffectAction;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.effects.DiscountEffect;
import com.gempukku.lotro.game.effects.PayTwilightCostEffect;
import com.gempukku.lotro.game.effects.PlayEventEffect;
import com.gempukku.lotro.game.effects.Effect;

import java.util.Collections;

public class PlayEventAction extends AbstractCostToEffectAction {
    private final PhysicalCard _eventPlayed;
    private final boolean _requiresRanger;

    private boolean _cardRemoved;

    private PlayEventEffect _playCardEffect;
    private boolean _cardPlayed;

    private boolean _cardDiscarded;

    private boolean _discountResolved;
    private boolean _discountApplied;

    private final Zone _playedFrom;

    public PlayEventAction(PhysicalCard card) {
        this(card, false);
    }

    public PlayEventAction(PhysicalCard card, boolean requiresRanger) {
        _eventPlayed = card;
        _requiresRanger = requiresRanger;

        _playedFrom = card.getZone();

        setText("Play " + GameUtils.getFullName(_eventPlayed));
    }

    public PhysicalCard getEventPlayed() {
        return _eventPlayed;
    }

    @Override
    public Type getType() {
        return Type.PLAY_CARD;
    }

    public boolean isRequiresRanger() {
        return _requiresRanger;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _eventPlayed;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _eventPlayed;
    }

    @Override
    public Effect nextEffect(DefaultGame game) {
        if (!_cardRemoved) {
            _cardRemoved = true;
            final Zone playedFromZone = _eventPlayed.getZone();

            game.getGameState().sendMessage(_eventPlayed.getOwner() + " plays " + GameUtils.getCardLink(_eventPlayed) + " from " + playedFromZone.getHumanReadable());
            game.getGameState().removeCardsFromZone(_eventPlayed.getOwner(), Collections.singleton(_eventPlayed));

            if (playedFromZone == Zone.HAND)
                game.getGameState().addCardToZone(game, _eventPlayed, Zone.VOID_FROM_HAND);
            else
                game.getGameState().addCardToZone(game, _eventPlayed, Zone.VOID);

            if (playedFromZone == Zone.DECK) {
                game.getGameState().sendMessage(_eventPlayed.getOwner() + " shuffles their deck");
                game.getGameState().shuffleDeck(_eventPlayed.getOwner());
            }

            game.getGameState().eventPlayed(_eventPlayed);
        }

        if (!_discountResolved) {
            final DiscountEffect discount = getNextPotentialDiscount();
            if (discount != null) {
                if (_eventPlayed.getBlueprint().getSide() == Side.SHADOW) {
                    int twilightCost = game.getModifiersQuerying().getTwilightCost(game, _eventPlayed, null, 0, false);
                    int requiredDiscount = Math.max(0, twilightCost - game.getGameState().getTwilightPool() - getProcessedDiscount() - getPotentialDiscount(game));
                    discount.setMinimalRequiredDiscount(requiredDiscount);
                }
                return discount;
            } else {
                _discountResolved = true;
            }
        }

        if (!_discountApplied) {
            _discountApplied = true;
            int twilightModifier = -getProcessedDiscount();
            insertCost(new PayTwilightCostEffect(_eventPlayed, twilightModifier));
        }

        if (!isCostFailed()) {
            Effect cost = getNextCost();
            if (cost != null)
                return cost;

            if (!_cardPlayed) {
                _cardPlayed = true;
                _playCardEffect = new PlayEventEffect(this, _playedFrom, _eventPlayed, _requiresRanger, isPaidToil());
                return _playCardEffect;
            }

            if (_playCardEffect != null && !_playCardEffect.getPlayEventResult().isEventCancelled()) {
                Effect effect = getNextEffect();
                if (effect != null)
                    return effect;
            }
        }

        if (!_cardDiscarded && (_eventPlayed.getZone() == Zone.VOID || _eventPlayed.getZone() == Zone.VOID_FROM_HAND)) {
            _cardDiscarded = true;
            game.getGameState().removeCardsFromZone(_eventPlayed.getOwner(), Collections.singleton(_eventPlayed));
            game.getGameState().addCardToZone(game, _eventPlayed, Zone.DISCARD);
        }

        return null;
    }
}
