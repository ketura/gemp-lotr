package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.CardCollection;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoloDraftDefinitionsTest {
    @Test
    public void loadSoloDraftDefinitions() {
        SoloDraftDefinitions soloDraftDefinitions = new SoloDraftDefinitions(null, null, null, null);
    }

    @Test
    public void playerDrafting() {
        SoloDraftDefinitions soloDraftDefinitions = new SoloDraftDefinitions(null, null, null, null);
        SoloDraft soloDraft = soloDraftDefinitions.getSoloDraft("hobbit_draft");
        assertTrue(soloDraft.hasNextStage(0, 0));
        Iterable<SoloDraft.DraftChoice> availableChoices = soloDraft.getAvailableChoices(0, 0);
        String choiceId = availableChoices.iterator().next().getChoiceId();

        CardCollection firstPickCards = soloDraft.getCardsForChoiceId(choiceId, 0, 0);
        assertFalse(firstPickCards.getAll().isEmpty());

        assertTrue(soloDraft.getCardsForChoiceId("madeUpChoiceId", 0, 0).getAll().isEmpty());

        assertTrue(soloDraft.hasNextStage(0, 1));
        Iterable<SoloDraft.DraftChoice> secondChoices = soloDraft.getAvailableChoices(0, 1);
        String secondChoiceId = secondChoices.iterator().next().getChoiceId();

        assertFalse(soloDraft.getCardsForChoiceId(secondChoiceId, 0, 1).getAll().isEmpty());
        assertTrue(soloDraft.getCardsForChoiceId("madeUpChoiceId", 0, 1).getAll().isEmpty());
    }
}