package com.gempukku.lotro.cards;

import com.gempukku.lotro.actions.OptionalTriggerAction;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.game.DefaultGame;

import java.util.List;

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
    List<OptionalTriggerAction> getOptionalAfterTriggerActions(String playerId, DefaultGame game,
                                                                      EffectResult effectResult,
                                                                      LotroPhysicalCard self);

}
