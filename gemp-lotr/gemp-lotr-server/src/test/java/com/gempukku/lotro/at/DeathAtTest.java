package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class DeathAtTest extends AbstractAtTest {
    @Test
    public void charactersDontDieIfPrintedVitalityEqualToWoundsButCurrentVitalityMoreThanZero() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl boromir = new PhysicalCardImpl(100, "1_96", P1, _library.getLotroCardBlueprint("1_96"));
        PhysicalCardImpl sagaOfElendil = new PhysicalCardImpl(101, "1_114", P1, _library.getLotroCardBlueprint("1_114"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, boromir, Zone.FREE_CHARACTERS);
        _game.getGameState().attachCard(_game, sagaOfElendil, boromir);
        _game.getGameState().addWound(boromir);
        _game.getGameState().addWound(boromir);
        _game.getGameState().addWound(boromir);

        // End fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");

        // End shadow phase
        assertEquals(Phase.SHADOW, _game.getGameState().getCurrentPhase());
        playerDecided(P2, "");

        // Pass in Regroup phase
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P2, "");

        // Decide not to move
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, getMultipleDecisionIndex(_userFeedback.getAwaitingDecision(P1), "No"));

        // Fellowship of player2
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());

        // Boromir is still not dead
        assertEquals(Zone.FREE_CHARACTERS, boromir.getZone());
    }

    @Test
    public void charactersDieIfCurrentVitalityIsZero() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl boromir = new PhysicalCardImpl(100, "1_96", P1, _library.getLotroCardBlueprint("1_96"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, boromir, Zone.FREE_CHARACTERS);
        _game.getGameState().addWound(boromir);
        _game.getGameState().addWound(boromir);
        _game.getGameState().addWound(boromir);

        // End fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");

        // Boromir is dead
        assertEquals(Zone.DEAD, boromir.getZone());
    }
}
