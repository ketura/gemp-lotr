package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.PhysicalCard;

public interface PhysicalCardVisitor {
    public boolean visitPhysicalCard(PhysicalCard physicalCard);
}
