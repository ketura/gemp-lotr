package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class SpotEffect extends UnrespondableEffect {
    private Filter _filter;

    public SpotEffect(Filter filter) {
        _filter = filter;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _filter);
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        // Do nothing
    }
}
