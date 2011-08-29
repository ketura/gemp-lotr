package com.gempukku.lotro.logic.timing;

public interface Cost {
    public boolean canBePayed();

    public Effect getPayingCostEffect();
}
