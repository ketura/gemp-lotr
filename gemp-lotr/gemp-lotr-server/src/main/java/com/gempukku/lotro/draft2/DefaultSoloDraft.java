package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

public class DefaultSoloDraft implements SoloDraft {
    private String _format;
    private SoloDraftPicks _picks;

    public DefaultSoloDraft(String format, SoloDraftPicks picks) {
        _format = format;
        _picks = picks;
    }

    @Override
    public CardCollection initializeNewCollection(long seed) {
        return new DefaultCardCollection();
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
