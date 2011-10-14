package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.effects.PlayCardEffect;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayPermanentAction extends AbstractCostToEffectAction {
    private PhysicalCard _permanentPlayed;
    private Zone _zone;

    private boolean _cardRemoved;

    private Iterator<Effect> _preCostIterator;

    private boolean _cardPutIntoPlay;

    private Effect _playCardEffect;
    private boolean _cardPlayed;

    private boolean _cardDiscarded;

    public PlayPermanentAction(PhysicalCard card, Zone zone, int twilightModifier) {
        _permanentPlayed = card;
        _zone = zone;

        List<Effect> preCostEffects = new LinkedList<Effect>();
        preCostEffects.add(new SendMessageEffect(card.getOwner() + " plays " + GameUtils.getCardLink(card) + " from " + card.getZone().getHumanReadable()));
        appendCost(new PayTwilightCostEffect(card, twilightModifier));
        if (card.getZone() == Zone.DECK)
            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));

        _preCostIterator = preCostEffects.iterator();

        _playCardEffect = new PlayCardEffect(card);
    }

    @Override
    public PhysicalCard getActionSource() {
        return _permanentPlayed;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play " + _permanentPlayed.getBlueprint().getName();
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        if (_preCostIterator.hasNext())
            return _preCostIterator.next();

        if (!_cardRemoved) {
            _cardRemoved = true;
            game.getGameState().removeCardsFromZone(Collections.singleton(_permanentPlayed));
        }

        if (!isCostFailed()) {
            Effect cost = getNextCost();
            if (cost != null)
                return cost;

            if (!_cardPutIntoPlay) {
                _cardPutIntoPlay = true;
                game.getGameState().addCardToZone(_permanentPlayed, _zone);
                game.getGameState().startAffecting(_permanentPlayed, game.getModifiersEnvironment());
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
                game.getGameState().addCardToZone(_permanentPlayed, Zone.DISCARD);
            }
        }

        return null;
    }
}
