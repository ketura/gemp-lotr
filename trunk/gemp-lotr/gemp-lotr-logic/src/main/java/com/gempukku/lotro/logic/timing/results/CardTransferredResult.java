package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class CardTransferredResult extends EffectResult {
    private PhysicalCard _transferredCard;
    private PhysicalCard _transferredFrom;
    private PhysicalCard _transferredTo;

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
