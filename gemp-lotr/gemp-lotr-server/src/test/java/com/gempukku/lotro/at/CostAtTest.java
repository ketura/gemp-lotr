package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.PhysicalCardImpl;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.cards.LotroDeck;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CostAtTest extends AbstractAtTest {
    @Test
    public void playOnCostReduction() throws DecisionResultInvalidException, CardNotFoundException {
        Map<String, LotroDeck> decks = new HashMap<>();

        LotroDeck deck = createSimplestDeck();
        // Frodo, Reluctant Adventurer
        deck.setRingBearer("2_102");
        decks.put(P1, deck);
        addPlayerDeck(P2, decks, null);

        initializeGameWithDecks(decks);

        PhysicalCardImpl hobbitSword = createCard(P1, "1_299");

        _game.getGameState().addCardToZone(_game, hobbitSword, Zone.HAND);

        skipMulligans();

        // Play Hobbit Sword (on Frodo) - should be free
        final int twilightPool = _game.getGameState().getTwilightPool();
        playerDecided(P1, "0");
        assertEquals(twilightPool, _game.getGameState().getTwilightPool());
    }

    @Test
    public void exertExtraCost() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        final PhysicalCardImpl azog = createCard(P2, "32_28");
        final PhysicalCardImpl fimbul = createCard(P2, "30_33");

        _game.getGameState().addCardToZone(_game, azog, Zone.HAND);
        _game.getGameState().addCardToZone(_game, fimbul, Zone.HAND);

        skipMulligans();

        _game.getGameState().setTwilight(20);

        playerDecided(P1, "");

        playerDecided(P2, getCardActionId(P2, "Play Azog"));
        playerDecided(P2, getCardActionId(P2, "Play Fimbul"));

        assertEquals(1, _game.getGameState().getWounds(azog));
    }
}
