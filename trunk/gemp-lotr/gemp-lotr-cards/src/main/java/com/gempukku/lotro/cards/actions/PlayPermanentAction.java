package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayPermanentAction implements CostToEffectAction {
    private PhysicalCard _source;

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

    public PlayPermanentAction(PhysicalCard card, Zone zone) {
        this(card, zone, 0);
    }

    public PlayPermanentAction(PhysicalCard card, Zone zone, int twilightModifier) {
        _source = card;

        List<Effect> preCostEffects = new LinkedList<Effect>();
        preCostEffects.add(new RemoveCardFromZoneEffect(card));
        preCostEffects.add(new PayTwilightCostEffect(card, twilightModifier));
        if (card.getZone() == Zone.DECK)
            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));

        _preCostIterator = preCostEffects.iterator();

        _putCardIntoPlayEffect = new PutCardIntoPlayEffect(card, zone);

        _playCardEffect = new TriggeringEffect(new PlayCardResult(card));

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
        return "Play " + _source.getBlueprint().getName();
    }

    @Override
    public Effect nextEffect() {
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

        return null;
    }
}
