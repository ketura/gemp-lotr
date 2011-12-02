package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecisionType;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class ArcheryAtTest extends AbstractAtTest {
    @Test
    public void archeryWorksBothWays() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl legolas = new PhysicalCardImpl(100, "1_51", P1, _library.getLotroCardBlueprint("1_51"));
        PhysicalCardImpl archerMinion = new PhysicalCardImpl(101, "4_138", P2, _library.getLotroCardBlueprint("4_138"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, legolas, Zone.FREE_CHARACTERS);

        // End fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, archerMinion, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        assertEquals(Phase.SHADOW, _game.getGameState().getCurrentPhase());
        playerDecided(P2, "");

        // End maneuver phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End archery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        AwaitingDecision archeryWoundDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, archeryWoundDecision.getDecisionType());
        assertEquals(2, ((String[]) archeryWoundDecision.getDecisionParameters().get("cardId")).length);

        playerDecided(P1, String.valueOf(legolas.getCardId()));

        assertEquals(1, _game.getGameState().getWounds(legolas));
        assertEquals(1, _game.getGameState().getWounds(archerMinion));
        assertEquals(Phase.ASSIGNMENT, _game.getGameState().getCurrentPhase());
    }

    @Test
    public void archeryBonusesDontLeakToOtherSide() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        initializeSimplestGame(extraCards);

        PhysicalCardImpl legolas = new PhysicalCardImpl(100, "1_51", P1, _library.getLotroCardBlueprint("1_51"));
        PhysicalCardImpl archerMinion = new PhysicalCardImpl(101, "4_138", P2, _library.getLotroCardBlueprint("4_138"));
        PhysicalCardImpl doubleShot = new PhysicalCardImpl(102, "1_38", P1, _library.getLotroCardBlueprint("1_38"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, legolas, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, doubleShot, Zone.HAND);

        // End fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, archerMinion, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        assertEquals(Phase.SHADOW, _game.getGameState().getCurrentPhase());
        playerDecided(P2, "");

        // End maneuver phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Play Double Shot
        playerDecided(P1, "0");
        // End archery phase
        playerDecided(P2, "");
        playerDecided(P1, "");

        AwaitingDecision archeryWoundDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, archeryWoundDecision.getDecisionType());
        assertEquals(2, ((String[]) archeryWoundDecision.getDecisionParameters().get("cardId")).length);

        playerDecided(P1, String.valueOf(legolas.getCardId()));

        assertEquals(1, _game.getGameState().getWounds(legolas));
        assertEquals(2, _game.getGameState().getWounds(archerMinion));
        assertEquals(Phase.ASSIGNMENT, _game.getGameState().getCurrentPhase());
    }
}
