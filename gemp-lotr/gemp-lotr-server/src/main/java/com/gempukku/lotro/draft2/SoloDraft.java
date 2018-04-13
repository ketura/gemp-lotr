package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;

public interface SoloDraft {
    CardCollection initializeNewCollection(long seed);

    Iterable<DraftChoice> getAvailableChoices(long seed, int stage);

    CardCollection getCardsForChoiceId(String choiceId, long seed, int stage);

    boolean hasNextStage(long seed, int stage);

    String getFormat();

    interface DraftChoice {
        String getChoiceId();
        String getBlueprintId();
        String getChoiceUrl();
    }
}
