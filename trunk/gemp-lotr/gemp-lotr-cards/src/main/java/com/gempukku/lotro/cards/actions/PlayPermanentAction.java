package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.effects.PlayCardEffect;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayPermanentAction extends AbstractCostToEffectAction {
    private PhysicalCard _source;

    private Iterator<Effect> _preCostIterator;

    private Effect _putCardIntoPlayEffect;
    private boolean _cardPutIntoPlay;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private Effect _discardCardEffect;
    private boolean _cardDiscarded;

    public PlayPermanentAction(PhysicalCard card, Zone zone, int twilightModifier) {
        _source = card;

        List<Effect> preCostEffects = new LinkedList<Effect>();
        preCostEffects.add(new SendMessageEffect(card.getOwner() + " plays " + GameUtils.getCardLink(card) + " from " + card.getZone().getHumanReadable()));
        preCostEffects.add(new RemoveCardFromZoneEffect(card));
        appendCost(new PayTwilightCostEffect(card, twilightModifier));
        if (card.getZone() == Zone.DECK)
            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));

        _preCostIterator = preCostEffects.iterator();

        _putCardIntoPlayEffect = new PutCardIntoPlayEffect(card, zone);

        _playCardEffect = new PlayCardEffect(card);

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
        return "Play " + _source.getBlueprint().getName();
    }

    @Override
    public Effect nextEffect() {
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

        return null;
    }
}
