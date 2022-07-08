package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;

public class Card_01_311_ErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("sam", "51_311");

                    put("orc", "1_272");
                }}
        );
    }

    @Test
    public void SamStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 1E
         * Title: *Sam
         * Subtitle: Son of Hamfast
         * Side: Free Peoples
         * Culture: Shire
         * Twilight Cost: 2
         * Type: Companion
         * Subtype: Hobbit
         * Strength: 3
         * Vitality: 4
         * Signet: Aragorn
         * Errata Game Text: Fellowship: Exert Sam and another companion to remove a burden.
         * Response: If Frodo dies, make Sam the Ring-bearer (resistance 5).
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl sam = scn.GetFreepsCard("sam");

        assertTrue(sam.getBlueprint().isUnique());
        assertEquals(2, sam.getBlueprint().getTwilightCost());

        assertEquals(3, sam.getBlueprint().getStrength());
        assertEquals(4, sam.getBlueprint().getVitality());
        assertEquals(Signet.ARAGORN, sam.getBlueprint().getSignet());

    }

    @Test
    public void FellowshipActionExertsTwiceToRemoveABurden() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

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
        GenericCardTestHelper scn = GetScenario();

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
