package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.PutCardIntoDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveCardFromZoneEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.PlayCardEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayEventAction implements CostToEffectAction {
    private PhysicalCard _source;

    private Iterator<Effect> _preCostIterator;

    private List<Effect> _costs = new ArrayList<Effect>();
    private int _nextCostIndex;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private List<Effect> _effects = new ArrayList<Effect>();
    private int _nextEffectIndex;

    private Effect _discardCardEffect;
    private boolean _cardDiscarded;

    public PlayEventAction(PhysicalCard card) {
        _source = card;

        List<Effect> preCostEffects = new LinkedList<Effect>();
        preCostEffects.add(new RemoveCardFromZoneEffect(card));
        preCostEffects.add(new PayTwilightCostEffect(card));
        if (card.getZone() == Zone.DECK)
            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));

        _preCostIterator = preCostEffects.iterator();

        _playCardEffect = new PlayCardEffect(card);

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
        }

        if (!_cardDiscarded) {
            _cardDiscarded = true;
            return _discardCardEffect;
        }

        return null;
    }
}
