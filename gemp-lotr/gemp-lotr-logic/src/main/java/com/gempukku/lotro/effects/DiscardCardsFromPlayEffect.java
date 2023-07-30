package com.gempukku.lotro.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DiscardCardsFromPlayEffect extends AbstractPreventableCardEffect {
    private final LotroPhysicalCard _source;
    private final String _performingPlayer;

    public DiscardCardsFromPlayEffect(String performingPlayer, LotroPhysicalCard source, Filterable... filters) {
        super(filters);
        _performingPlayer = performingPlayer;
        _source = source;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        if (_source == null)
            return Filters.any;
        return Filters.canBeDiscarded(_performingPlayer, _source);
    }

    public String getPerformingPlayer() {
        return _performingPlayer;
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_DISCARD_FROM_PLAY;
    }


    @Override
    public String getText(DefaultGame game) {
        Collection<LotroPhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Discard " + getAppendedTextNames(cards);
    }

    protected void forEachDiscardedByEffectCallback(Collection<LotroPhysicalCard> discardedCards) {

    }

    @Override
    protected void playoutEffectOn(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        Set<LotroPhysicalCard> discardedCards = new HashSet<>();

        Set<LotroPhysicalCard> toMoveFromZoneToDiscard = new HashSet<>();

        GameState gameState = game.getGameState();

        DiscardUtils.cardsToChangeZones(game, cards, discardedCards, toMoveFromZoneToDiscard);

        discardedCards.addAll(cards);
        toMoveFromZoneToDiscard.addAll(cards);

        gameState.removeCardsFromZone(_performingPlayer, toMoveFromZoneToDiscard);

        for (LotroPhysicalCard card : toMoveFromZoneToDiscard)
            gameState.addCardToZone(game, card, Zone.DISCARD);

        if (_source != null && discardedCards.size() > 0)
            game.getGameState().sendMessage(_performingPlayer + " discards " + getAppendedNames(discardedCards) + " from play using " + GameUtils.getCardLink(_source));

        for (LotroPhysicalCard discardedCard : discardedCards)
            game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(_source, getPerformingPlayer(), discardedCard));

        forEachDiscardedByEffectCallback(cards);
    }
}
