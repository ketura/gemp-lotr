package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiscardFromHandAtTest extends AbstractAtTest {
    @Test
    public void foulCreationDiscardsCardAndDrawsCards() throws Exception {
        initializeSimplestGame();

        final PhysicalCardImpl legolas = createCard(P1, "1_50");
        _game.getGameState().addCardToZone(_game, legolas, Zone.FREE_CHARACTERS);

        PhysicalCardImpl foulCreation = createCard(P1, "1_44");
        _game.getGameState().addCardToZone(_game, foulCreation, Zone.HAND);

        PhysicalCardImpl lurtz = createCard(P2, "1_127");
        _game.getGameState().addCardToZone(_game, lurtz, Zone.HAND);

        skipMulligans();

        playerDecided(P1, getCardActionId(P1, "Play Foul Creation"));

        playerDecided(P1, "");

        playerDecided(P1, "0");

        assertEquals(Zone.DISCARD, lurtz.getZone());
    }
}
