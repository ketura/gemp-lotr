package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RevealCardsFromHandResult;

import java.util.List;

public abstract class RevealRandomCardsFromHandEffect extends AbstractEffect {
    private PhysicalCard _source;
    private int _count;
    private String _actingPlayer;
    private String _playerHand;

    protected RevealRandomCardsFromHandEffect(String actingPlayer, String handOfPlayer, PhysicalCard source, int count) {
        _actingPlayer = actingPlayer;
        _playerHand = handOfPlayer;
        _source = source;
        _count = count;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.REVEAL_CARDS_FROM_HAND;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_actingPlayer.equals(_playerHand) || game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _playerHand)) {
            List<PhysicalCard> randomCards = GameUtils.getRandomCards(game.getGameState().getHand(_playerHand), _count);
            if (randomCards.size() > 0)
                game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed cards from " + _playerHand + " hand at random - " + getAppendedNames(randomCards));
            cardsRevealed(randomCards);
            return new FullEffectResult(new EffectResult[]{new RevealCardsFromHandResult(_source, _playerHand, randomCards)}, randomCards.size() == _count, randomCards.size() == _count);
        }
        return new FullEffectResult(null, false, false);
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        if (game.getGameState().getHand(_playerHand).size() < _count)
            return false;
        return _actingPlayer.equals(_playerHand) || game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _playerHand);
    }

    protected abstract void cardsRevealed(List<PhysicalCard> revealedCards);
}
