package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;

public interface DraftChoiceDefinition {
    Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage);

    CardCollection getCardsForChoiceId(String choiceId, long seed, int stage);
}
