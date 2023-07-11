package com.gempukku.lotro.communication;

import com.gempukku.lotro.cards.PhysicalCard;

public interface PlayerPrivateChannel {
    public void putCardIntoHand(PhysicalCard physicalCard);

    public void removeCardFromHand(PhysicalCard physicalCard);
}
