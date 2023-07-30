package com.gempukku.lotro.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

import java.util.Collections;

public class TransferToShadowEffect extends AbstractEffect {
    private final LotroPhysicalCard _card;

    public TransferToShadowEffect(LotroPhysicalCard card) {
        _card = card;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _card.getZone().isInPlay();
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().removeCardsFromZone(_card.getOwner(), Collections.singleton(_card));
            game.getGameState().addCardToZone(game, _card, Zone.SHADOW_CHARACTERS);
            cardTransferredCallback();
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    protected void cardTransferredCallback() {
    }
}
