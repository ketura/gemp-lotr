package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PayPlayOnTwilightCostEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.discount.ToilDiscountEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayCardEffect;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.*;

public class AttachPermanentAction extends AbstractCostToEffectAction implements DiscountableAction {
    private PhysicalCard _cardToAttach;

    private boolean _cardRemoved;

    private ChooseActiveCardEffect _chooseTargetEffect;
    private boolean _targetChosen;

    private Iterator<Effect> _preCostIterator;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private boolean _cardDiscarded;

    private boolean _exertTarget;

    private PhysicalCard _target;

    private boolean _discountResolved;
    private DiscountEffect _discountEffect;

    private boolean _discountApplied;

    private int _twilightModifier;

    public AttachPermanentAction(final LotroGame game, final PhysicalCard card, Filter filter, final Map<Filter, Integer> attachCostModifiers, final int twilightModifier) {
        _cardToAttach = card;

        final Zone zone = card.getZone();

        _chooseTargetEffect =
                new ChooseActiveCardEffect(null, card.getOwner(), "Attach " + card.getBlueprint().getName() + ". Choose target to attach to", filter) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard target) {
                        _target = target;
                        if (_exertTarget) {
                            appendCost(
                                    new ExertCharactersEffect(target, target));
                        }

                        int modifier = twilightModifier;
                        for (Map.Entry<Filter, Integer> filterIntegerEntry : attachCostModifiers.entrySet())
                            if (filterIntegerEntry.getKey().accepts(game.getGameState(), game.getModifiersQuerying(), target))
                                modifier += filterIntegerEntry.getValue();

                        _twilightModifier = modifier;

                        List<Effect> preCostEffects = new LinkedList<Effect>();
                        preCostEffects.add(new SendMessageEffect(card.getOwner() + " plays " + GameUtils.getCardLink(card) + " from " + zone.getHumanReadable() + " on " + GameUtils.getCardLink(target)));
                        if (zone == Zone.DECK)
                            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));

                        _preCostIterator = preCostEffects.iterator();

                        _playCardEffect = new PlayCardEffect(zone, card, target, null);
                    }
                };
    }

    @Override
    public Type getType() {
        return Type.PLAY_CARD;
    }

    @Override
    public void setDiscountEffect(DiscountEffect discountEffect) {
        _discountEffect = discountEffect;
    }

    public PhysicalCard getTarget() {
        return _target;
    }

    public void setExertTarget(boolean exertTarget) {
        _exertTarget = exertTarget;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _cardToAttach;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _cardToAttach;
    }

    @Override
    public String getText(LotroGame game) {
        return "Attach " + _cardToAttach.getBlueprint().getName();
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        if (!_cardRemoved) {
            _cardRemoved = true;
            boolean fromHand = (_cardToAttach.getZone() == Zone.HAND);
            game.getGameState().removeCardsFromZone(_cardToAttach.getOwner(), Collections.singleton(_cardToAttach));
            if (fromHand)
                game.getGameState().addCardToZone(game, _cardToAttach, Zone.VOID);
        }

        if (!_targetChosen) {
            _targetChosen = true;
            return _chooseTargetEffect;
        }

        if (_preCostIterator.hasNext())
            return _preCostIterator.next();

        if (!_discountResolved) {
            _discountResolved = true;
            if (_discountEffect == null) {
                int toilCount = game.getModifiersQuerying().getKeywordCount(game.getGameState(), _cardToAttach, Keyword.TOIL);
                if (toilCount > 0) {
                    _discountEffect = new ToilDiscountEffect(this, _cardToAttach, _cardToAttach.getOwner(), _cardToAttach.getBlueprint().getCulture(), toilCount);
                }
            }
            if (_discountEffect != null) {
                int requiredDiscount = 0;
                if (_cardToAttach.getBlueprint().getSide() == Side.SHADOW) {
                    int twilightCost = game.getModifiersQuerying().getPlayOnTwilightCost(game.getGameState(), _cardToAttach, _target);
                    int underPool = twilightCost - game.getGameState().getTwilightPool();
                    if (underPool > 0)
                        requiredDiscount = underPool;
                }
                _discountEffect.setMinimalRequiredDiscount(requiredDiscount);
                return _discountEffect;
            }
        }

        if (!_discountApplied) {
            _discountApplied = true;
            if (_discountEffect != null)
                _twilightModifier -= _discountEffect.getDiscountPaidFor();
            insertCost(new PayPlayOnTwilightCostEffect(_cardToAttach, _target, _twilightModifier));
        }

        if (_playCardEffect != null) {
            if (!isCostFailed()) {
                Effect cost = getNextCost();
                if (cost != null)
                    return cost;

                if (!_cardPlayed) {
                    _cardPlayed = true;
                    return _playCardEffect;
                }

                Effect effect = getNextEffect();
                if (effect != null)
                    return effect;
            } else {
                if (!_cardDiscarded) {
                    _cardDiscarded = true;
                    if (_cardToAttach.getZone() != null)
                        game.getGameState().removeCardsFromZone(_cardToAttach.getOwner(), Collections.singleton(_cardToAttach));
                    game.getGameState().addCardToZone(game, _cardToAttach, Zone.DISCARD);
                }
            }
        } else {
            if (!_cardDiscarded) {
                _cardDiscarded = true;
                if (_cardToAttach.getZone() != null)
                    game.getGameState().removeCardsFromZone(_cardToAttach.getOwner(), Collections.singleton(_cardToAttach));
                game.getGameState().addCardToZone(game, _cardToAttach, Zone.DISCARD);
            }
        }

        return null;
    }
}
