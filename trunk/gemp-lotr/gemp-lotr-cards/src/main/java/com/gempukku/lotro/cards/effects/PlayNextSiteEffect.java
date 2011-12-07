package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.timing.Action;

public class PlayNextSiteEffect extends PlaySiteEffect {
    public PlayNextSiteEffect(Action action, String playerId) {
        super(action, playerId, null, 0);
    }

    @Override
    protected int getSiteNumberToPlay(LotroGame game) {
        return game.getGameState().getCurrentSiteNumber() + 1;
    }
}
