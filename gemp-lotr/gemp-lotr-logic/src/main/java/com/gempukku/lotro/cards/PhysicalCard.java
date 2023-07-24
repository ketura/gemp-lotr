package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;

public interface PhysicalCard extends Filterable {
    Zone getZone();
    String getBlueprintId();
    String getImageUrl();
    String getOwner();
    String getCardController();
    int getCardId();
    LotroCardBlueprint getBlueprint();
    PhysicalCard getAttachedTo();
    PhysicalCard getStackedOn();
    void setWhileInZoneData(Object object);
    Object getWhileInZoneData();

}
