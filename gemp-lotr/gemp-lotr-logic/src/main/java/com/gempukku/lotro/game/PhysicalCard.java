package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Zone;

public interface PhysicalCard {
    public Zone getZone();

    public String getBlueprintId();

    public String getOwner();

    public String getController();

    public int getCardId();

    public LotroCardBlueprint getBlueprint();

    public PhysicalCard getAttachedTo();

    public PhysicalCard getStackedOn();

    public void storeData(Object object);

    public Object getData();

    public void removeData();
}
