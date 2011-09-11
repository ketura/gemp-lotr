package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.*;

public class AttachPermanentAction implements CostToEffectAction {
    private PhysicalCard _source;

    private Effect _removeCardEffect;
    private boolean _cardRemoved;

    private Effect _chooseTargetEffect;
    private boolean _targetChosen;

    private Iterator<Effect> _preCostIterator;

    private List<Effect> _costs = new ArrayList<Effect>();
    private int _nextCostIndex;

    private Effect _putCardIntoPlayEffect;
    private boolean _cardPutIntoPlay;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private List<Effect> _effects = new ArrayList<Effect>();
    private int _nextEffectIndex;

    private Effect _discardCardEffect;
    private boolean _cardDiscarded;

    public AttachPermanentAction(final LotroGame game, final PhysicalCard card, Filter filter, final Map<Filter, Integer> attachCostModifiers) {
        _source = card;

        _removeCardEffect = new RemoveCardFromZoneEffect(card);

        _chooseTargetEffect =
                new ChooseActiveCardEffect(card.getOwner(), "Choose target to attach to", filter) {
                    @Override
                    protected void cardSelected(PhysicalCard target) {
                        _putCardIntoPlayEffect = new AttachCardEffect(_source, target);

                        int modifier = 0;
                        for (Map.Entry<Filter, Integer> filterIntegerEntry : attachCostModifiers.entrySet())
                            if (filterIntegerEntry.getKey().accepts(game.getGameState(), game.getModifiersQuerying(), target))
                                modifier += filterIntegerEntry.getValue();

                        List<Effect> preCostEffects = new LinkedList<Effect>();
                        preCostEffects.add(new PayTwilightCostEffect(card, modifier));
                        if (card.getZone() == Zone.DECK)
                            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));

                        _preCostIterator = preCostEffects.iterator();

                        _playCardEffect = new TriggeringEffect(new PlayCardResult(card, target));
                    }
                };
        _discardCardEffect = new PutCardIntoDiscardEffect(card);
    }

    public void addCost(Effect cost) {
        _costs.add(cost);
    }

    public void addEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public PhysicalCard getActionSource() {
        return _source;
    }

    @Override
    public Keyword getType() {
        return null;
    }

    @Override
    public String getText() {
        return "Attach " + _source.getBlueprint().getName();
    }

    @Override
    public Effect nextEffect() {
        if (!_cardRemoved) {
            _cardRemoved = true;
            return _removeCardEffect;
        }

        if (!_targetChosen) {
            _targetChosen = true;
            return _chooseTargetEffect;
        }

        if (!_chooseTargetEffect.isCancelled() && !_chooseTargetEffect.isFailed()) {
            if (_preCostIterator.hasNext())
                return _preCostIterator.next();

            boolean anyCostCancelledOrFailed = false;
            for (int i = 0; i < _nextCostIndex; i++) {
                Effect cost = _costs.get(i);
                if (cost.isCancelled() || cost.isFailed())
                    anyCostCancelledOrFailed = true;
            }

            if (!anyCostCancelledOrFailed) {
                if (_nextCostIndex < _costs.size()) {
                    Effect cost = _costs.get(_nextCostIndex);
                    _nextCostIndex++;
                    return cost;
                }

                if (!_cardPutIntoPlay) {
                    _cardPutIntoPlay = true;
                    return _putCardIntoPlayEffect;
                }

                if (!_cardPlayed) {
                    _cardPlayed = true;
                    return _playCardEffect;
                }

                if (!_playCardEffect.isCancelled() && !_playCardEffect.isFailed()) {
                    if (_nextEffectIndex < _effects.size()) {
                        Effect effect = _effects.get(_nextEffectIndex);
                        _nextEffectIndex++;
                        return effect;
                    }
                }
            } else {
                if (!_cardDiscarded) {
                    _cardDiscarded = true;
                    return _discardCardEffect;
                }
            }
        } else {
            if (!_cardDiscarded) {
                _cardDiscarded = true;
                return _discardCardEffect;
            }
        }

        return null;
    }
}
