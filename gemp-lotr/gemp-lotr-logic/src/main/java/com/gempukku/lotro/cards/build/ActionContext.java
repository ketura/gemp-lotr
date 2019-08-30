package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;

import java.util.Collection;

public interface ActionContext {
    void setValueToMemory(String memory, String value);

    String getValueFromMemory(String memory);

    void setCardMemory(String memory, PhysicalCard card);

    void setCardMemory(String memory, Collection<? extends PhysicalCard> cards);

    Collection<? extends PhysicalCard> getCardsFromMemory(String memory);

    PhysicalCard getCardFromMemory(String memory);
}
