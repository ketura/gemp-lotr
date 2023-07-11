package com.gempukku.lotro.at;

import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerListAtTest extends AbstractAtTest {
    @Test
    public void testAllPlayers() throws DecisionResultInvalidException {
        initializeSimplestGame();
        skipMulligans();

        final String[] allPlayers = GameUtils.getAllPlayers(_game);
        assertEquals(2, allPlayers.length);
        assertEquals(P1, allPlayers[0]);
        assertEquals(P2, allPlayers[1]);
    }
}
