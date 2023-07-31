package com.gempukku.lotro.cards.lotronly;

import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.common.Zone;

public interface LotroPhysicalCard extends PhysicalCard {
    Zone getZone();
    String getBlueprintId();
    String getImageUrl();

    String getOwner();

    String getCardController();

    int getCardId();

    LotroCardBlueprint getBlueprint();

    LotroPhysicalCard getAttachedTo();

    LotroPhysicalCard getStackedOn();

    void setWhileInZoneData(Object object);

    Object getWhileInZoneData();

    void setSiteNumber(Integer number);

    Integer getSiteNumber();


}
