package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_006Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("stealth1", "2_106");
                    put("stealth2", "2_106");

                    put("chief", "101_6");
                    put("snarler", "101_7");

                }}
        );
    }


    @Test
    public void ChiefStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: *Great Wolf Chief
         * Side: Shadow
         * Culture: Sauron
         * Twilight Cost: 5
         * Type: Minion
         * Subtype: Warg
         * Strength: 10
         * Vitality: 3
         * Home Site: 6
         * Game Text: Tracker.  Fierce.  The site number of this minion is -1 for each stealth card you can spot.
         * Each time you play another [sauron] Warg, you may make the Free Peoples player exert a companion.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl chief = scn.GetFreepsCard("chief");

        assertTrue(chief.getBlueprint().isUnique());
        assertEquals(5, chief.getBlueprint().getTwilightCost());
        assertEquals(10, chief.getBlueprint().getStrength());
        assertEquals(3, chief.getBlueprint().getVitality());
        assertEquals(6, chief.getBlueprint().getSiteNumber());
        assertTrue(scn.HasKeyword(chief, Keyword.TRACKER));
        assertTrue(scn.HasKeyword(chief, Keyword.FIERCE));
    }

    @Test
    public void ChiefSiteNumberReducedForEachStealthCard() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl stealth1 = scn.GetFreepsCard("stealth1");
        PhysicalCardImpl stealth2 = scn.GetFreepsCard("stealth2");

        scn.FreepsMoveCardToSupportArea(stealth1, stealth2);

        PhysicalCardImpl chief = scn.GetShadowCard("chief");

        scn.ShadowMoveCharToTable(chief);

        scn.StartGame();

        // 6 base, -2 for the 2 Nice Imitations on the table.
        assertEquals(4, scn.GetSiteNumber(chief));
    }

    @Test
    public void ChiefExertsACompanionWhenEachWargPlayed() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();

        PhysicalCardImpl chief = scn.GetShadowCard("chief");
        PhysicalCardImpl snarler = scn.GetShadowCard("snarler");

        scn.ShadowMoveCharToTable(chief);
        scn.ShadowMoveCardToHand(snarler);

        scn.StartGame();

        scn.SetTwilight(10);
        scn.FreepsSkipCurrentPhaseAction();

        scn.ShadowPlayCard(snarler);
        assertTrue(scn.ShadowHasOptionalTriggerAvailable());
        scn.ShadowAcceptOptionalTrigger();

        assertEquals(1, scn.GetWoundsOn(frodo));
    }


}
