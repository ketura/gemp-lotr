package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

public abstract class CompletePhysicalCardVisitor implements PhysicalCardVisitor {
    @Override
    public boolean visitPhysicalCard(LotroPhysicalCard physicalCard) {
        doVisitPhysicalCard(physicalCard);
        return false;
    }

    protected abstract void doVisitPhysicalCard(LotroPhysicalCard physicalCard);
}
