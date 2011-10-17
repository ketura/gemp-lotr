package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;

public interface PhysicalCard extends Filterable {
    public Zone getZone();

    public String getBlueprintId();

    public String getOwner();

    public String getCardController();

    public int getCardId();

    public LotroCardBlueprint getBlueprint();

    public PhysicalCard getAttachedTo();

    public PhysicalCard getStackedOn();

    public void storeData(Object object);

    public Object getData();

    public void removeData();
}
