package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class CardTransferredResult extends EffectResult {
    private final PhysicalCard _transferredCard;
    private final PhysicalCard _transferredFrom;
    private final PhysicalCard _transferredTo;

    public CardTransferredResult(PhysicalCard transferredCard, PhysicalCard transferredFrom, PhysicalCard transferredTo) {
        super(EffectResult.Type.CARD_TRANSFERRED);
        _transferredCard = transferredCard;
        _transferredFrom = transferredFrom;
        _transferredTo = transferredTo;
    }

    public PhysicalCard getTransferredCard() {
        return _transferredCard;
    }

    public PhysicalCard getTransferredFrom() {
        return _transferredFrom;
    }

    public PhysicalCard getTransferredTo() {
        return _transferredTo;
    }
}
