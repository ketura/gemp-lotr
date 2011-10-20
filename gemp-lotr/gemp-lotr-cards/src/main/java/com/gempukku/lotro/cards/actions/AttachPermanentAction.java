package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.AttachCardEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PayPlayOnTwilightCostEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
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

public class AttachPermanentAction extends AbstractCostToEffectAction {
    private PhysicalCard _cardToAttach;

    private boolean _cardRemoved;

    private ChooseActiveCardEffect _chooseTargetEffect;
    private boolean _targetChosen;

    private Iterator<Effect> _preCostIterator;

    private Effect _putCardIntoPlayEffect;
    private boolean _cardPutIntoPlay;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private boolean _cardDiscarded;

    private boolean _exertTarget;

    private PhysicalCard _target;

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

                        _putCardIntoPlayEffect = new AttachCardEffect(_cardToAttach, target);

                        int modifier = twilightModifier;
                        for (Map.Entry<Filter, Integer> filterIntegerEntry : attachCostModifiers.entrySet())
                            if (filterIntegerEntry.getKey().accepts(game.getGameState(), game.getModifiersQuerying(), target))
                                modifier += filterIntegerEntry.getValue();

                        List<Effect> preCostEffects = new LinkedList<Effect>();
                        preCostEffects.add(new SendMessageEffect(card.getOwner() + " plays " + GameUtils.getCardLink(card) + " from " + zone.getHumanReadable() + " on " + GameUtils.getCardLink(target)));
                        if (zone == Zone.DECK)
                            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));
                        appendCost(new PayPlayOnTwilightCostEffect(card, target, modifier));

                        _preCostIterator = preCostEffects.iterator();

                        _playCardEffect = new PlayCardEffect(card, target);
                    }
                };
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
            game.getGameState().removeCardsFromZone(Collections.singleton(_cardToAttach));
        }

        if (!_targetChosen) {
            _targetChosen = true;
            return _chooseTargetEffect;
        }

        if (_putCardIntoPlayEffect != null) {
            if (_preCostIterator.hasNext())
                return _preCostIterator.next();

            if (!isCostFailed()) {
                Effect cost = getNextCost();
                if (cost != null)
                    return cost;

                if (!_cardPutIntoPlay) {
                    _cardPutIntoPlay = true;
                    return _putCardIntoPlayEffect;
                }

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
                    game.getGameState().addCardToZone(game, _cardToAttach, Zone.DISCARD);
                }
            }
        } else {
            if (!_cardDiscarded) {
                _cardDiscarded = true;
                game.getGameState().addCardToZone(game, _cardToAttach, Zone.DISCARD);
            }
        }

        return null;
    }
}
