package com.gempukku.lotro.cards.unofficial.pc.errata.set3;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Aragorn_HttWCErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("aragorn", "53_38");
                    put("gimli", "1_13");

                    put("minion", "1_268");
                    put("tracker", "1_270");
                }}
        );
    }

    @Test
    public void AragornHasRanger() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");

        assertTrue(scn.HasKeyword(aragorn, Keyword.RANGER));
    }


    @Test
    public void AragornAbilityOnlyWorksDuringFellowship() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        scn.FreepsMoveCharToTable(aragorn);

        scn.StartGame();
        scn.SetTwilight(2);

        scn.FreepsSkipCurrentPhaseAction();

        // 2 for Frodo/Aragorn, 1 for the site, 2 in the pool, -1 for Aragorn's text
        assertEquals(4, scn.GetTwilight());


        scn.SkipToPhase(Phase.REGROUP);

        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        assertEquals(4, scn.GetTwilight());
        scn.FreepsChooseToMove();

        // 2 for Frodo/Aragorn, 1 for the site, 4 in the pool, no removal from Aragorn
        assertEquals(7, scn.GetTwilight());
    }

    @Test
    public void AragornAbilityOnlyWorksIfTwilightInPool() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        scn.FreepsMoveCharToTable(aragorn);

        scn.StartGame();

        scn.FreepsSkipCurrentPhaseAction();

        // 2 for Frodo/Aragorn, 1 for the site, no removal since there was nothing in the pool before moving
        assertEquals(3, scn.GetTwilight());

    }



}
