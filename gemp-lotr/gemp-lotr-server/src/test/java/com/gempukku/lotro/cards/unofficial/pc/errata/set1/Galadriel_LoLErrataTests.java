package com.gempukku.lotro.cards.unofficial.pc.errata.set1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;

public class Galadriel_LoLErrataTests
{
    protected GenericCardTestHelper GetSimplePlayScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("galadriel", "51_45");
                    put("elrond", "1_40");
                }}
        );
    }

    protected GenericCardTestHelper GetHome6AllyScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("galadriel", "51_45");
                    put("allyHome3_1", "1_60");
                    put("allyHome6_1", "1_56");
                    put("allyHome6_2", "1_57");
                    put("allyHome6_3", "1_34");
                }}
        );
    }

    @Test
    public void FellowshipActionExertsToDiscountAnElf() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetSimplePlayScenario();

        PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
        scn.FreepsMoveCharToTable(galadriel);
        scn.FreepsMoveCardToHand("elrond");

        scn.StartGame();

        assertEquals(Phase.FELLOWSHIP, scn.GetCurrentPhase());
        assertTrue(scn.FreepsCardActionAvailable(galadriel));

        assertEquals(0, scn.GetWoundsOn(galadriel));
        assertEquals(1, scn.GetFreepsHandCount());
        assertEquals(0, scn.GetTwilight());

        scn.FreepsUseCardAction(galadriel);

        assertEquals(1, scn.GetWoundsOn(galadriel));
        assertEquals(0, scn.GetFreepsHandCount());
        assertEquals(3, scn.GetTwilight());
    }


    @Test
    public void AllyHealsCappedAt2() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetHome6AllyScenario();
        scn.FreepsMoveCharToTable("galadriel");
        scn.FreepsMoveCharToTable("allyHome3_1");
        scn.FreepsMoveCharToTable("allyHome6_1");
        scn.FreepsMoveCharToTable("allyHome6_2");
        scn.FreepsMoveCharToTable("allyHome6_3");

        scn.FreepsAddWoundsToChar("galadriel", 1);
        scn.FreepsAddWoundsToChar("allyHome3_1", 1);
        scn.FreepsAddWoundsToChar("allyHome6_1", 1);
        scn.FreepsAddWoundsToChar("allyHome6_2", 1);
        scn.FreepsAddWoundsToChar("allyHome6_3", 1);

        scn.StartGame();

        assertEquals(Phase.BETWEEN_TURNS, scn.GetCurrentPhase());

        //There are 5 total elf allies in play: 1 is home 3, and 1 is Galadriel herself.  The rest should be eligible.
        assertEquals(3, scn.FreepsGetADParamAsList("cardId").size());
        assertEquals("0", scn.FreepsGetADParam("min"));
        assertEquals("2", scn.FreepsGetADParam("max"));
    }
}
