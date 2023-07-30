package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.EffectResult;

import java.util.Collection;

public interface ActionContext {
    void setValueToMemory(String memory, String value);

    String getValueFromMemory(String memory);

    void setCardMemory(String memory, LotroPhysicalCard card);

    void setCardMemory(String memory, Collection<? extends LotroPhysicalCard> cards);

    Collection<? extends LotroPhysicalCard> getCardsFromMemory(String memory);

    LotroPhysicalCard getCardFromMemory(String memory);

    String getPerformingPlayer();

    DefaultGame getGame();

    LotroPhysicalCard getSource();

    EffectResult getEffectResult();

    Effect getEffect();
}
