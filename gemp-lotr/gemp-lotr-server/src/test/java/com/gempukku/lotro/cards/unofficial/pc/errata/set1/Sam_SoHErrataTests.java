package com.gempukku.lotro.cards.unofficial.pc.errata.set1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;

public class Sam_SoHErrataTests
{
    protected GenericCardTestHelper GetSimpleScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("sam", "51_311");

                    put("orc", "1_272");
                }}
        );
    }

    @Test
    public void FellowshipActionExertsTwiceToRemoveABurden() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetSimpleScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        scn.FreepsMoveCharToTable(sam);

        scn.StartGame();

        assertEquals(Phase.FELLOWSHIP, scn.GetCurrentPhase());
        assertTrue(scn.FreepsCardActionAvailable(sam));

        assertEquals(0, scn.GetWoundsOn(sam));
        assertEquals(0, scn.GetWoundsOn(frodo));
        assertEquals(1, scn.GetBurdens());

        scn.FreepsUseCardAction(sam);

        assertEquals(1, scn.GetWoundsOn(sam));
        assertEquals(1, scn.GetWoundsOn(frodo));
        assertEquals(0, scn.GetBurdens());
    }


    @Test
    public void RBDeathMakesSamTheRB() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetSimpleScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl orc = scn.GetShadowCard("orc");
        scn.FreepsMoveCharToTable(sam);

        scn.StartGame();

        assertNotSame(scn.GetRingBearer(), sam);
        scn.AddWoundsToChar(frodo, 4);

        scn.SkipCurrentPhaseActions();

        assertTrue(scn.FreepsActionAvailable("Optional Trigger"));
    }
}
