package com.gempukku.lotro.cards.modifiers.spotting;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;

public class SimplePhysicalCard implements PhysicalCard {
    private LotroCardBlueprint _blueprint;

    public SimplePhysicalCard(LotroCardBlueprint blueprint) {
        _blueprint = blueprint;
    }

    @Override
    public PhysicalCard getAttachedTo() {
        return null;
    }

    @Override
    public LotroCardBlueprint getBlueprint() {
        return _blueprint;
    }

    @Override
    public String getBlueprintId() {
        return null;
    }

    @Override
    public int getCardId() {
        return 0;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public PhysicalCard getStackedOn() {
        return null;
    }

    @Override
    public Zone getZone() {
        return null;
    }

    @Override
    public void removeData() {
    }

    @Override
    public void storeData(Object object) {
    }
}
