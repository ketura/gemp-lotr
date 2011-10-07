package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Keyword;
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AttachPermanentAction extends AbstractCostToEffectAction {
    private PhysicalCard _source;

    private Effect _removeCardEffect;
    private boolean _cardRemoved;

    private ChooseActiveCardEffect _chooseTargetEffect;
    private boolean _targetChosen;

    private Iterator<Effect> _preCostIterator;

    private Effect _putCardIntoPlayEffect;
    private boolean _cardPutIntoPlay;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private Effect _discardCardEffect;
    private boolean _cardDiscarded;

    public AttachPermanentAction(final LotroGame game, final PhysicalCard card, Filter filter, final Map<Filter, Integer> attachCostModifiers, final int twilightModifier) {
        _source = card;

        _removeCardEffect = new RemoveCardFromZoneEffect(card);

        _chooseTargetEffect =
                new ChooseActiveCardEffect(null, card.getOwner(), "Attach " + card.getBlueprint().getName() + ". Choose target to attach to", filter) {
                    @Override
                    protected void cardSelected(PhysicalCard target) {
                        _putCardIntoPlayEffect = new AttachCardEffect(_source, target);

                        int modifier = twilightModifier;
                        for (Map.Entry<Filter, Integer> filterIntegerEntry : attachCostModifiers.entrySet())
                            if (filterIntegerEntry.getKey().accepts(game.getGameState(), game.getModifiersQuerying(), target))
                                modifier += filterIntegerEntry.getValue();

                        List<Effect> preCostEffects = new LinkedList<Effect>();
                        preCostEffects.add(new SendMessageEffect(card.getOwner() + " plays " + GameUtils.getCardLink(card) + " from " + card.getZone().getHumanReadable() + " on " + GameUtils.getCardLink(target)));
                        if (card.getZone() == Zone.DECK)
                            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));
                        appendCost(new PayPlayOnTwilightCostEffect(card, target, modifier));

                        _preCostIterator = preCostEffects.iterator();

                        _playCardEffect = new PlayCardEffect(card, target);
                    }
                };
        _discardCardEffect = new PutCardIntoDiscardEffect(card);
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
    public String getText(LotroGame game) {
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
