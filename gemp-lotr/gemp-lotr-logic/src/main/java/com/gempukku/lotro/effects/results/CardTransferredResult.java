package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

public class CardTransferredResult extends EffectResult {
    private final LotroPhysicalCard _transferredCard;
    private final LotroPhysicalCard _transferredFrom;
    private final LotroPhysicalCard _transferredTo;

    public CardTransferredResult(LotroPhysicalCard transferredCard, LotroPhysicalCard transferredFrom, LotroPhysicalCard transferredTo) {
        super(EffectResult.Type.CARD_TRANSFERRED);
        _transferredCard = transferredCard;
        _transferredFrom = transferredFrom;
        _transferredTo = transferredTo;
    }

    public LotroPhysicalCard getTransferredCard() {
        return _transferredCard;
    }

    public LotroPhysicalCard getTransferredFrom() {
        return _transferredFrom;
    }

    public LotroPhysicalCard getTransferredTo() {
        return _transferredTo;
    }
}
