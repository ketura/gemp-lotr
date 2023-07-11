package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.Action;

public class PlayNextSiteEffect extends PlaySiteEffect {
    public PlayNextSiteEffect(Action action, String playerId) {
        super(action, playerId, null, 0);
    }

    public PlayNextSiteEffect(Action action, String playerId, Filterable... extraSiteFilters) {
        super(action, playerId, null, 0, extraSiteFilters);
    }

    @Override
    protected int getSiteNumberToPlay(DefaultGame game) {
        return game.getGameState().getCurrentSiteNumber() + 1;
    }
}
