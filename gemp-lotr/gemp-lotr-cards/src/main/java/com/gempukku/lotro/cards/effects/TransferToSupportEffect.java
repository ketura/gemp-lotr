package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;

public class TransferToSupportEffect extends AbstractEffect {
    private PhysicalCard _card;

    public TransferToSupportEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone() == Zone.ATTACHED;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().removeCardsFromZone(Collections.singleton(_card));
            game.getGameState().addCardToZone(_card, Zone.SUPPORT);
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
