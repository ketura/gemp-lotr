package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_007Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("stealth1", "2_106");
                    put("stealth2", "2_106");

                    put("snarler", "101_7");

                }}
        );
    }


    @Test
    public void SnarlerStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: Hollin Snarler
         * Side: Shadow
         * Culture: Sauron
         * Twilight Cost: 3
         * Type: Minion
         * Subtype: Warg
         * Strength: 4
         * Vitality: 2
         * Home Site: 6
         * Game Text: Tracker.  Fierce.  The site number of this minion is -1 for each stealth card you can spot.
         * This minion is strength +1 for each wounded companion or stealth card you can spot.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl snarler = scn.GetFreepsCard("snarler");

        assertFalse(snarler.getBlueprint().isUnique());
        assertEquals(3, snarler.getBlueprint().getTwilightCost());
        assertEquals(4, snarler.getBlueprint().getStrength());
        assertEquals(3, snarler.getBlueprint().getVitality());
        assertEquals(6, snarler.getBlueprint().getSiteNumber());
        assertTrue(scn.HasKeyword(snarler, Keyword.TRACKER));
        assertTrue(scn.HasKeyword(snarler, Keyword.FIERCE));
    }

    @Test
    public void SnarlerSiteNumberReducedForEachStealthCard() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl stealth1 = scn.GetFreepsCard("stealth1");
        PhysicalCardImpl stealth2 = scn.GetFreepsCard("stealth2");

        scn.FreepsMoveCardToSupportArea(stealth1, stealth2);

        PhysicalCardImpl snarler = scn.GetShadowCard("snarler");

        scn.ShadowMoveCharToTable(snarler);

        scn.StartGame();

        // 6 base, -2 for the 2 Nice Imitations on the table.
        assertEquals(4, scn.GetSiteNumber(snarler));
    }

    @Test
    public void SnarlerStrengthIncreasesWithWoundedCompanionsAndStealth() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl stealth1 = scn.GetFreepsCard("stealth1");
        PhysicalCardImpl stealth2 = scn.GetFreepsCard("stealth2");

        scn.FreepsMoveCardToSupportArea(stealth1,stealth2);

        PhysicalCardImpl snarler = scn.GetShadowCard("snarler");

        scn.ShadowMoveCharToTable(snarler);

        scn.StartGame();

        scn.AddWoundsToChar(frodo, 1);
        scn.FreepsSkipCurrentPhaseAction();

        // Base of 4, +2 for stealth cards, +1 for wounded frodo
        assertEquals(7, scn.GetStrength(snarler));
    }


}
