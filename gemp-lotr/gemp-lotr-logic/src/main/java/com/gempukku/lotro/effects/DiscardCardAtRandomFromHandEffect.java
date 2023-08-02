package com.gempukku.lotro.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.results.DiscardCardFromHandResult;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DiscardCardAtRandomFromHandEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final String _playerId;
    private final boolean _forced;

    public DiscardCardAtRandomFromHandEffect(LotroPhysicalCard source, String playerId, boolean forced) {
        _source = source;
        _playerId = playerId;
        _forced = forced;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Discard card at random from hand";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getHand(_playerId).size() > 0
                && (!_forced || game.getModifiersQuerying().canDiscardCardsFromHand(game, _playerId, _source));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            List<? extends LotroPhysicalCard> hand = gameState.getHand(_playerId);
            LotroPhysicalCard randomCard = hand.get(ThreadLocalRandom.current().nextInt(hand.size()));
            gameState.sendMessage(_playerId + " randomly discards " + GameUtils.getCardLink(randomCard));
            gameState.removeCardsFromZone(_source.getOwner(), Collections.singleton(randomCard));
            gameState.addCardToZone(game, randomCard, Zone.DISCARD);
            game.getActionsEnvironment().emitEffectResult(new DiscardCardFromHandResult(_source, randomCard, _playerId, _forced));
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
