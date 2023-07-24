package com.gempukku.lotro.communication;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

public interface PlayerPrivateChannel {
    public void putCardIntoHand(LotroPhysicalCard physicalCard);

    public void removeCardFromHand(LotroPhysicalCard physicalCard);
}
