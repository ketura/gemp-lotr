package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

public interface DraftChoiceDefinition {
    Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool);

    CardCollection getCardsForChoiceId(String choiceId, long seed, int stage);
}
