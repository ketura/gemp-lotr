package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PayTwilightCostEffect;
import com.gempukku.lotro.cards.effects.RemoveCardFromZoneEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.effects.PlayEventEffect;
import com.gempukku.lotro.logic.effects.SendMessageEffect;
import com.gempukku.lotro.logic.effects.SendPlayEventMessageEffect;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayEventAction extends AbstractCostToEffectAction {
    private PhysicalCard _source;

    private Iterator<Effect> _preCostIterator;

    private PlayEventEffect _playCardEffect;
    private boolean _cardPlayed;

    private boolean _cardDiscarded;

    public PlayEventAction(PhysicalCard card) {
        this(card, false);
    }

    public PlayEventAction(PhysicalCard card, boolean requiresRanger) {
        _source = card;

        List<Effect> preCostEffects = new LinkedList<Effect>();
        preCostEffects.add(new SendMessageEffect(card.getOwner() + " plays " + GameUtils.getCardLink(card) + " from " + card.getZone().getHumanReadable()));
        preCostEffects.add(new SendPlayEventMessageEffect(card));
        preCostEffects.add(new RemoveCardFromZoneEffect(card));
        appendCost(new PayTwilightCostEffect(card));
        if (card.getZone() == Zone.DECK)
            preCostEffects.add(new ShuffleDeckEffect(card.getOwner()));

        _preCostIterator = preCostEffects.iterator();

        _playCardEffect = new PlayEventEffect(card);
        if (requiresRanger)
            _playCardEffect.setRequiresRanger(true);
    }

    @Override
    public PhysicalCard getActionSource() {
        return _source;
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

            if (!_cardPlayed) {
                _cardPlayed = true;
                return _playCardEffect;
            }

            if (!_playCardEffect.isCancelled()) {
                Effect effect = getNextEffect();
                if (effect != null)
                    return effect;
            }
        }

        if (!_cardDiscarded) {
            _cardDiscarded = true;
            return new AbstractSuccessfulEffect() {
                @Override
                public String getText(LotroGame game) {
                    return null;
                }

                @Override
                public EffectResult.Type getType() {
                    return null;
                }

                @Override
                public EffectResult[] playEffect(LotroGame game) {
                    game.getGameState().addCardToZone(_source, _playCardEffect.getTargetZone());
                    return null;
                }
            };
        }

        return null;
    }
}
