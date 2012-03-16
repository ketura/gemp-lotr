package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscardCardsFromPlayEffect extends AbstractPreventableCardEffect {
    private PhysicalCard _source;
    private String _performingPlayer;

    public DiscardCardsFromPlayEffect(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        _source = source;
    }

    public DiscardCardsFromPlayEffect(PhysicalCard source, Filterable... filters) {
        super(filters);
        _source = source;
    }

    public DiscardCardsFromPlayEffect(String performingPlayer, PhysicalCard source, Filterable... filters) {
        super(filters);
        _performingPlayer = performingPlayer;
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (_source == null || modifiersQuerying.canBeDiscardedFromPlay(gameState, physicalCard, _source));
            }
        };
    }

    public String getPerformingPlayer() {
        final String performingPlayer = _performingPlayer;
        if (performingPlayer != null)
            return performingPlayer;
        return _source.getOwner();
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_DISCARD_FROM_PLAY;
    }


    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Discard " + getAppendedTextNames(cards);
    }

    @Override
    protected void playoutEffectOn(LotroGame game, Collection<PhysicalCard> cards) {
        Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();

        Set<PhysicalCard> toMoveFromZoneToDiscard = new HashSet<PhysicalCard>();

        GameState gameState = game.getGameState();

        for (PhysicalCard card : cards) {
            discardedCards.add(card);
            toMoveFromZoneToDiscard.add(card);

            List<PhysicalCard> attachedCards = gameState.getAttachedCards(card);
            discardedCards.addAll(attachedCards);
            toMoveFromZoneToDiscard.addAll(attachedCards);

            List<PhysicalCard> stackedCards = gameState.getStackedCards(card);
            toMoveFromZoneToDiscard.addAll(stackedCards);
        }

        String sourcePlayer = null;
        if (_source != null)
            sourcePlayer = _source.getOwner();

        gameState.removeCardsFromZone(sourcePlayer, toMoveFromZoneToDiscard);

        for (PhysicalCard card : toMoveFromZoneToDiscard)
            gameState.addCardToZone(game, card, Zone.DISCARD);

        if (_source != null && discardedCards.size() > 0)
            game.getGameState().sendMessage(_source.getOwner() + " discards " + getAppendedNames(discardedCards) + " from play using " + GameUtils.getCardLink(_source));

        for (PhysicalCard discardedCard : discardedCards)
            game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(discardedCard));
    }
}
