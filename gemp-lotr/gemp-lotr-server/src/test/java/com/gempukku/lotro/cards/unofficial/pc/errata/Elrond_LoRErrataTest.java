package com.gempukku.lotro.cards.unofficial.pc.errata;

import com.gempukku.lotro.cards.GenericCardTest;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;

public class Elrond_LoRErrataTest
{

    protected GenericCardTest GetSimpleDeckScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("elrond", "21_1040");
                    put("randomcard", "1_3");
                }}
        );
    }

    protected GenericCardTest GetSimpleSpotScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("elrond", "21_1040");
                    put("gandalf", "1_72");
                    put("arwen", "1_30");
                }}
        );
    }

    protected GenericCardTest GetHome3AllyScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("elrond", "21_1040");
                    put("allyHome3_1", "1_60");
                    put("allyHome3_2", "1_27");
                    put("allyHome6_1", "1_56");
                    put("allyHome6_2", "1_57");
                }}
        );
    }

    @Test
    public void FellowshipActionExertsTwiceToDrawACard() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetSimpleDeckScenario();
        PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");

        scn.FreepsMoveCharToTable(elrond);

        scn.StartGame();

        assertEquals(Phase.FELLOWSHIP, scn.GetCurrentPhase());
        assertTrue(scn.FreepsActionAvailable("Use Elrond"));

        assertEquals(0, scn.GetWoundsOn(elrond));
        assertEquals(0, scn.GetFreepsHandCount());
        assertEquals(1, scn.GetFreepsDeckCount());

        scn.FreepsUseAction("Use Elrond");

        assertEquals(2, scn.GetWoundsOn(elrond));
        assertEquals(1, scn.GetFreepsHandCount());
        assertEquals(0, scn.GetFreepsDeckCount());
    }

    @Test
    public void CardCanPlayIfGandalfInPlay() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetSimpleSpotScenario();
        scn.FreepsMoveCardToHand("elrond");
        scn.FreepsMoveCardToHand("gandalf");

        scn.StartGame();

        assertEquals(Phase.FELLOWSHIP, scn.GetCurrentPhase());
        assertFalse(scn.FreepsActionAvailable("Play Elrond"));

        scn.FreepsPlayCharFromHand("gandalf");
        assertTrue(scn.FreepsActionAvailable("Play Elrond"));
    }

    @Test
    public void CardCanPlayIfElfInPlay() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetSimpleSpotScenario();
        scn.FreepsMoveCardToHand("elrond");
        scn.FreepsMoveCardToHand("arwen");

        scn.StartGame();

        assertEquals(Phase.FELLOWSHIP, scn.GetCurrentPhase());
        assertFalse(scn.FreepsActionAvailable("Play Elrond"));

        scn.FreepsPlayCharFromHand("arwen");
        assertTrue(scn.FreepsActionAvailable("Play Elrond"));
    }

    @Test
    public void AllyHealsCappedAt2() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetHome3AllyScenario();
        scn.FreepsMoveCharToTable("elrond");
        scn.FreepsMoveCharToTable("allyHome3_1");
        scn.FreepsMoveCharToTable("allyHome3_2");
        scn.FreepsMoveCharToTable("allyHome6_1");
        scn.FreepsMoveCharToTable("allyHome6_2");

        scn.FreepsAddWoundsToChar("elrond", 1);
        scn.FreepsAddWoundsToChar("allyHome3_1", 1);
        scn.FreepsAddWoundsToChar("allyHome3_2", 1);
        scn.FreepsAddWoundsToChar("allyHome6_1", 1);
        scn.FreepsAddWoundsToChar("allyHome6_2", 1);

        scn.StartGame();

        assertEquals(Phase.BETWEEN_TURNS, scn.GetCurrentPhase());

        assertEquals(3, scn.FreepsGetADParamAsList("cardId").size());
        assertEquals("0", scn.FreepsGetADParam("min"));
        assertEquals("2", scn.FreepsGetADParam("max"));
    }
}
