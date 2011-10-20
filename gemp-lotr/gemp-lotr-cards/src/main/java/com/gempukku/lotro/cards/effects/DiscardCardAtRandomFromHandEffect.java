package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromHandResult;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DiscardCardAtRandomFromHandEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerId;
    private boolean _forced;

    public DiscardCardAtRandomFromHandEffect(PhysicalCard source, String playerId, boolean forced) {
        _source = source;
        _playerId = playerId;
        _forced = forced;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard card at random from hand";
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_HAND;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getHand(_playerId).size() > 0
                && (!_forced || game.getModifiersQuerying().canDiscardCardsFromHand(game.getGameState(), _playerId, _source));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            List<? extends PhysicalCard> hand = gameState.getHand(_playerId);
            PhysicalCard randomCard = hand.get(new Random().nextInt(hand.size()));
            gameState.sendMessage(_playerId + " randomly discards " + GameUtils.getCardLink(randomCard));
            gameState.removeCardsFromZone(Collections.singleton(randomCard));
            gameState.addCardToZone(game, randomCard, Zone.DISCARD);
            return new FullEffectResult(new EffectResult[]{new DiscardCardsFromHandResult(_source, Collections.singleton(randomCard), _forced)}, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
