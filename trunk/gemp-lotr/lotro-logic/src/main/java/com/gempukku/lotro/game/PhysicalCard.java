package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Zone;

public interface PhysicalCard {
    public Zone getZone();

    public String getOwner();

    public int getCardId();

    public LotroCardBlueprint getBlueprint();

    public PhysicalCard getAttachedTo();
}
