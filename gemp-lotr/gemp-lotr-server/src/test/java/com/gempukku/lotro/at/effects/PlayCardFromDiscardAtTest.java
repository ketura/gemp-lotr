package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayCardFromDiscardAtTest extends AbstractAtTest {
    @Test
    public void cantPlayCardDiscardedWithTheyAreComing() throws Exception {
        initializeSimplestGame();

        final PhysicalCardImpl theyAreComing = createCard(P2,  "1_196");
        final PhysicalCardImpl goblinSneakInDiscard = createCard(P2, "1_181");

        _game.getGameState().addCardToZone(_game, theyAreComing, Zone.SUPPORT);
        _game.getGameState().addCardToZone(_game, goblinSneakInDiscard, Zone.DISCARD);

        for (int i = 0; i < 3; i++) {
            final PhysicalCardImpl goblinRunner = createCard(P2, "1_178");
            _game.getGameState().addCardToZone(_game, goblinRunner, Zone.HAND);
        }

        skipMulligans();

        _game.getGameState().setTwilight(10);

        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");

        playerDecided(P2, getCardActionId(P2, "Use They Are Coming"));

        Map<String, Object> decisionParameters = _userFeedback.getAwaitingDecision(P2).getDecisionParameters();
        String[] cardId = (String[]) decisionParameters.get("cardId");
        assertEquals(1, cardId.length);
        assertEquals(String.valueOf(goblinSneakInDiscard.getCardId()), cardId[0]);
    }

    @Test
    public void flameOfUdunCantPlayCardIfNotEnoughTwilight() throws Exception {
        initializeSimplestGame();

        skipMulligans();

        // Fellowship phase
        playerDecided(P1, "");

        // Shadow phase
        playerDecided(P2, "");

        // End regroup phase
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");
        playerDecided(P2, "");

        final PhysicalCardImpl goblinRunner = createCard(P2, "1_178");
        _game.getGameState().addCardToZone(_game, goblinRunner, Zone.HAND);

        final PhysicalCardImpl balrog = createCard(P2,  "2_52");
        _game.getGameState().addCardToZone(_game, balrog, Zone.HAND);

        final PhysicalCardImpl goblinSneakInDiscard = createCard(P2, "1_181");
        _game.getGameState().addCardToZone(_game, goblinSneakInDiscard, Zone.DISCARD);

        _game.getGameState().setTwilight(18);

        // Move again
        playerDecided(P1, "0");

        playerDecided(P2, getCardActionId(P2, "Play Goblin Runner"));
        playerDecided(P2, getCardActionId(P2, "Play The Balrog"));
        assertNull(getCardActionId(P2, "Use The Balrog"));
    }

    @Test
    public void flameOfUdunCanPlayCardIfEnoughTwilight() throws Exception {
        initializeSimplestGame();

        skipMulligans();

        // Fellowship phase
        playerDecided(P1, "");

        // Shadow phase
        playerDecided(P2, "");

        // End regroup phase
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");
        playerDecided(P2, "");

        final PhysicalCardImpl goblinRunner = createCard(P2, "1_178");
        _game.getGameState().addCardToZone(_game, goblinRunner, Zone.HAND);

        final PhysicalCardImpl balrog = createCard(P2,  "2_52");
        _game.getGameState().addCardToZone(_game, balrog, Zone.HAND);

        final PhysicalCardImpl goblinSneakInDiscard = createCard(P2, "1_181");
        _game.getGameState().addCardToZone(_game, goblinSneakInDiscard, Zone.DISCARD);

        _game.getGameState().setTwilight(20);

        // Move again
        playerDecided(P1, "0");

        playerDecided(P2, getCardActionId(P2, "Play Goblin Runner"));
        playerDecided(P2, getCardActionId(P2, "Play The Balrog"));
        playerDecided(P2, getCardActionId(P2, "Use The Balrog"));
        assertEquals(Zone.SHADOW_CHARACTERS, goblinSneakInDiscard.getZone());
    }
}
