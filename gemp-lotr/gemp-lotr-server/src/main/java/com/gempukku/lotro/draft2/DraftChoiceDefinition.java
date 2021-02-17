package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import java.util.List;

public interface DraftChoiceDefinition {
    Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool);

    CardCollection getCardsForChoiceId(String choiceId, long seed, int stage);
}
