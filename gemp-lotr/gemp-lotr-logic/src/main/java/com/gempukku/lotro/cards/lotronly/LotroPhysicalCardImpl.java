package com.gempukku.lotro.cards.lotronly;

import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.cards.PhysicalCardImpl;

public class LotroPhysicalCardImpl extends PhysicalCardImpl {

    protected LotroPhysicalCardImpl _attachedTo;
    protected LotroPhysicalCardImpl _stackedOn;

    public LotroPhysicalCardImpl(int cardId, String blueprintId, String owner, LotroCardBlueprint blueprint) {
        super(cardId, blueprintId, owner, blueprint);
    }

    public void attachTo(LotroPhysicalCardImpl physicalCard) {
        _attachedTo = physicalCard;
    }

    public void stackOn(LotroPhysicalCardImpl physicalCard) {
        _stackedOn = physicalCard;
    }

}