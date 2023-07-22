package com.gempukku.lotro.game.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.Effect;

public interface Action {
    enum Type {
        PLAY_CARD, SPECIAL_ABILITY, TRIGGER, TRANSFER, RECONCILE, RESOLVE_DAMAGE, OTHER,
        TRIBBLE_POWER
    }

    Type getType();

    PhysicalCard getActionSource();

    void setActionTimeword(Phase phase);

    PhysicalCard getActionAttachedToCard();

    void setVirtualCardAction(boolean virtualCardAction);

    boolean isVirtualCardAction();

    void setPerformingPlayer(String playerId);

    String getPerformingPlayer();

    Phase getActionTimeword();

    String getText(DefaultGame game);

    Effect nextEffect(DefaultGame game);
}
