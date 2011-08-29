package com.gempukku.lotro.game;

public abstract class CompletePhysicalCardVisitor implements PhysicalCardVisitor {
    @Override
    public boolean visitPhysicalCard(PhysicalCard physicalCard) {
        doVisitPhysicalCard(physicalCard);
        return false;
    }

    protected abstract void doVisitPhysicalCard(PhysicalCard physicalCard);
}
