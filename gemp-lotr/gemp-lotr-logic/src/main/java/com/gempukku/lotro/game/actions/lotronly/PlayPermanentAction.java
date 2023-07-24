package com.gempukku.lotro.game.actions.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.effects.DiscountEffect;
import com.gempukku.lotro.game.effects.PayTwilightCostEffect;
import com.gempukku.lotro.game.effects.PlayCardEffect;
import com.gempukku.lotro.game.effects.Effect;

import java.util.Collections;

public class PlayPermanentAction extends AbstractCostToEffectAction {
    private final LotroPhysicalCard _permanentPlayed;
    private int _twilightModifier;
    private final boolean _ignoreRoamingPenalty;

    private boolean _cardRemoved;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private boolean _cardDiscarded;

    private boolean _discountResolved;
    private boolean _discountApplied;

    private boolean _skipShuffling;
    private final Zone _fromZone;
    private final Zone _toZone;
    private LotroPhysicalCard _playedFromCard;

    public PlayPermanentAction(LotroPhysicalCard card, Zone zone, int twilightModifier, boolean ignoreRoamingPenalty) {
        _permanentPlayed = card;
        setText("Play " + GameUtils.getFullName(_permanentPlayed));
        setPerformingPlayer(card.getOwner());
        _twilightModifier = twilightModifier;
        _ignoreRoamingPenalty = ignoreRoamingPenalty;

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
    public LotroPhysicalCard getActionSource() {
        return _permanentPlayed;
    }

    @Override
    public LotroPhysicalCard getActionAttachedToCard() {
        return _permanentPlayed;
    }

    @Override
    public Effect nextEffect(DefaultGame game) {
//        game.getGameState().sendMessage("DEBUG: Calling nextEffect from lotronly/PlayPermanentAction");
        if (!_cardRemoved) {
            _cardRemoved = true;
            final Zone playedFromZone = _permanentPlayed.getZone();
            game.getGameState().sendMessage(_permanentPlayed.getOwner() + " plays " + GameUtils.getCardLink(_permanentPlayed) +
                    " from " + playedFromZone.getHumanReadable() + " to " + _toZone.getHumanReadable());
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

        if (!_discountResolved) {
            final DiscountEffect discount = getNextPotentialDiscount();
            if (discount != null) {
                if (_permanentPlayed.getBlueprint().getSide() == Side.SHADOW) {
                    int twilightCost = game.getModifiersQuerying().getTwilightCost(game, _permanentPlayed, null, _twilightModifier, _ignoreRoamingPenalty);
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
            _twilightModifier -= getProcessedDiscount();
            insertCost(new PayTwilightCostEffect(_permanentPlayed, _twilightModifier, _ignoreRoamingPenalty));
        }

        if (!isCostFailed()) {
            Effect cost = getNextCost();
            if (cost != null)
                return cost;

            if (!_cardPlayed) {
                _cardPlayed = true;
                _playCardEffect = new PlayCardEffect(_fromZone, _permanentPlayed, _toZone, _playedFromCard);
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
