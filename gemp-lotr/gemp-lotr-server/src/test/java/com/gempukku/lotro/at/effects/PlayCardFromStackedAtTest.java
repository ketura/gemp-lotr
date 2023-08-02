package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayCardFromStackedAtTest extends AbstractAtTest {
    @Test
    public void playFromGoblinSwarms() throws Exception {
        initializeSimplestGame();

        skipMulligans();

        final LotroPhysicalCardImpl goblinSwarms = createCard(P2, "1_183");
        _game.getGameState().addCardToZone(_game, goblinSwarms, Zone.SUPPORT);

        final LotroPhysicalCardImpl goblinRunner = createCard(P2, "1_178");
        _game.getGameState().stackCard(_game, goblinRunner, goblinSwarms);

        _game.getGameState().setTwilight(20);

        // Fellowship phase
        playerDecided(P1, "");

        playerDecided(P2, getCardActionId(P2, "Use Goblin Swarms"));

        assertEquals(Zone.SHADOW_CHARACTERS, goblinRunner.getZone());
    }
}
