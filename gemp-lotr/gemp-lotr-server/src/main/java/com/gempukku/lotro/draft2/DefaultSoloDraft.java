package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;

public class DefaultSoloDraft implements SoloDraft {
    private String _format;
    private SoloDraftStartingPool _startingPool;
    private SoloDraftPicks _picks;

    public DefaultSoloDraft(String format, SoloDraftStartingPool startingPool, SoloDraftPicks picks) {
        _format = format;
        _startingPool = startingPool;
        _picks = picks;
    }

    @Override
    public CardCollection initializeNewCollection(long seed) {
        return _startingPool.getCardCollection(seed);
    }

    @Override
    public CardCollection getAvailableChoices(long seed, int stage) {
        return _picks.getChoices(seed, stage);
    }

    @Override
    public boolean hasNextStage(long seed, int stage) {
        return _picks.hasNextStage(seed, stage);
    }

    @Override
    public String getFormat() {
        return _format;
    }
}
