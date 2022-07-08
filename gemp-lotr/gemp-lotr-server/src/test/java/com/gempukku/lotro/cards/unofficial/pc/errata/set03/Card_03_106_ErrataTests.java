package com.gempukku.lotro.cards.unofficial.pc.errata.set03;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_03_106_ErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("bill", "53_106");
                    put("sam", "1_311");
                }}
        );
    }

    @Test
    public void BillStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 1E
         * Title: *Bill the Pony
         * Side: Free Peoples
         * Culture: Shire
         * Twilight Cost: 0
         * Type: Possession
         * Subtype: Pony
         * Errata Game Text: Stealth.  Bearer must be Sam.
         * Each site's Shadow number is -1. Discard this possession when at an underground site.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl bill = scn.GetFreepsCard("bill");

        assertTrue(bill.getBlueprint().isUnique());
        assertEquals(0, bill.getBlueprint().getTwilightCost());

        assertTrue(scn.HasKeyword(bill, Keyword.STEALTH));
    }

    @Test
    public void BillCanBeBorneBySam() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl bill = scn.GetFreepsCard("bill");
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");

        scn.FreepsMoveCardToHand(bill, sam);

        scn.StartGame();


        assertFalse(scn.FreepsCardPlayAvailable(bill));
        scn.FreepsPlayCard(sam);
        assertTrue(scn.FreepsCardPlayAvailable(bill));
        scn.FreepsPlayCard(bill);

        Assert.assertTrue(scn.IsAttachedTo(bill, sam));
    }


    @Test
    public void BillReducesTwilight() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl bill = scn.GetFreepsCard("bill");
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");

        scn.FreepsMoveCardToHand(bill, sam);

        scn.StartGame();

        scn.FreepsPlayCard(sam);
        scn.FreepsPlayCard(bill);

        // 2 for Frodo/Sam, 1 for the site, -1 for Bill
        assertEquals(2, scn.GetTwilight());
    }


    @Test
    public void BillDiscardedWhenMovingToUnderground() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl bill = scn.GetFreepsCard("bill");
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");

        scn.FreepsMoveCardToHand(bill, sam);

        scn.ApplyAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.UNDERGROUND));

        scn.StartGame();

        scn.FreepsPlayCard(sam);
        scn.FreepsPlayCard(bill);

        scn.FreepsSkipCurrentPhaseAction();

        assertFalse(scn.IsAttachedTo(bill, sam));
        assertEquals(Zone.DISCARD, bill.getZone());
    }

}
