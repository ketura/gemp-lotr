package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;

public interface SoloDraft {
    CardCollection initializeNewCollection(long seed);

    CardCollection getAvailableChoices(long seed, int stage);

    boolean hasNextStage(long seed, int stage);

    String getFormat();
}
