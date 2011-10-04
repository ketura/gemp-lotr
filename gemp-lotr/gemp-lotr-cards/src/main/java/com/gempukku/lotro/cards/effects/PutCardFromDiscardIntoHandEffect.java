package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

public class PutCardFromDiscardIntoHandEffect extends AbstractEffect {
    private PhysicalCard _card;

    public PutCardFromDiscardIntoHandEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public String getText(LotroGame game) {
        return "Put card from discard into hand";
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone() == Zone.DISCARD;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (game.getModifiersQuerying().canDrawCardAndIncrement(game.getGameState(), _card.getOwner())
                && _card.getZone() == Zone.DISCARD) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_card.getOwner() + " puts " + GameUtils.getCardLink(_card) + " from discard into his or her hand");
            gameState.removeCardFromZone(_card);
            gameState.addCardToZone(_card, Zone.HAND);

            return new FullEffectResult(new EffectResult[]{new DrawCardOrPutIntoHandResult(_card.getOwner(), 1)}, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
