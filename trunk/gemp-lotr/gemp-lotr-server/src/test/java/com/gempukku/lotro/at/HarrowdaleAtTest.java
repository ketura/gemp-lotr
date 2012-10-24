package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class HarrowdaleAtTest extends AbstractAtTest {
    @Test
    public void movesFromHarrowdale() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl harrowdale = new PhysicalCardImpl(100, "11_243", P1, _library.getLotroCardBlueprint("11_243"));
        harrowdale.setSiteNumber(1);
        _game.getGameState().removeCardsFromZone(P1, Collections.singleton(_game.getGameState().getSite(1)));
        _game.getGameState().addCardToZone(_game, harrowdale, Zone.ADVENTURE_PATH);

        skipMulligans();

        // Fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        assertEquals(1, ((DefaultActionsEnvironment) _game.getActionsEnvironment()).getUntilStartOfPhaseActionProxies(Phase.REGROUP).size());
        playerDecided(P1, "");

        // Move first time (from Harrowdale)

        // Shadow phase
        assertEquals(1, ((DefaultActionsEnvironment) _game.getActionsEnvironment()).getUntilStartOfPhaseActionProxies(Phase.REGROUP).size());
        playerDecided(P2, "");

        // End regroup phase
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Move again
        playerDecided(P1, "0");

        // Shadow phase
        assertEquals(Phase.SHADOW, _game.getGameState().getCurrentPhase());
        assertEquals(0, ((DefaultActionsEnvironment) _game.getActionsEnvironment()).getUntilStartOfPhaseActionProxies(Phase.REGROUP).size());
        playerDecided(P2, "");

        // End regroup phase
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");
        playerDecided(P2, "");
    }

    @Test
    public void movesThroughHarrowdale() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl harrowdale = new PhysicalCardImpl(100, "11_243", P1, _library.getLotroCardBlueprint("11_243"));
        harrowdale.setSiteNumber(2);
        _game.getGameState().addCardToZone(_game, harrowdale, Zone.ADVENTURE_PATH);

        skipMulligans();

        // Fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        assertNull(((DefaultActionsEnvironment) _game.getActionsEnvironment()).getUntilStartOfPhaseActionProxies(Phase.REGROUP));
        playerDecided(P1, "");

        // Move first time (to Harrowdale)

        // Shadow phase
        assertEquals(1, ((DefaultActionsEnvironment) _game.getActionsEnvironment()).getUntilStartOfPhaseActionProxies(Phase.REGROUP).size());
        playerDecided(P2, "");

        // End regroup phase
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Move again (from Harrowdale in regroup)
        playerDecided(P1, "0");

        // Shadow phase
        assertEquals(Phase.SHADOW, _game.getGameState().getCurrentPhase());
        assertEquals(0, ((DefaultActionsEnvironment) _game.getActionsEnvironment()).getUntilStartOfPhaseActionProxies(Phase.REGROUP).size());
        playerDecided(P2, "");

        // End regroup phase
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        assertEquals(0, ((DefaultActionsEnvironment) _game.getActionsEnvironment()).getUntilStartOfPhaseActionProxies(Phase.REGROUP).size());
        playerDecided(P1, "");
        playerDecided(P2, "");
    }
}