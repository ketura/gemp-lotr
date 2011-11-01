package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface Action {
    public PhysicalCard getActionSource();

    public void setActionTimeword(Phase phase);

    public PhysicalCard getActionAttachedToCard();

    public void setPerformingPlayer(String playerId);

    public String getPerformingPlayer();

    public Phase getActionTimeword();

    public String getText(LotroGame game);

    public Effect nextEffect(LotroGame game);

    public boolean wasSuccessful();

    public boolean wasCarriedOut();
}
