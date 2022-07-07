package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StingErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("sting", "51_313");
                    put("sam", "1_311");
                    put("merry", "1_302");

                    put("orc1", "1_178");
                    put("orc2", "1_178");
                    put("orc3", "1_178");
                    put("scimitar1", "1_180");
                    put("scimitar2", "1_180");
                    put("scimitar3", "1_180");
                }}
        );
    }

    @Test
    public void StingStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 1E
         * Title: *Sting
         * Side: Free Peoples
         * Culture: Shire
         * Twilight Cost: 1
         * Strength: +2
         * Type: Possession
         * Subtype: Hand Weapon
         * Errata Game Text: Bearer must be Frodo.
         * Fellowship or Regroup: Exert Frodo to reveal 4 cards at random from an opponent's hand.  Remove (1) for each Orc revealed (limit (2)).
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl sting = scn.GetFreepsCard("sting");

        assertTrue(sting.getBlueprint().isUnique());
        assertEquals(1, sting.getBlueprint().getTwilightCost());
        assertEquals(2, sting.getBlueprint().getStrength());
    }


    @Test
    public void StingCanOnlyBeBorneByFrodo() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");

        scn.FreepsMoveCharToTable(sam);
        scn.FreepsMoveCardToHand(sting);

        scn.StartGame();

        scn.FreepsPlayCard(sting);

        //should have automatically gone to frodo as the only valid target
        Assert.assertEquals(Zone.ATTACHED, sting.getZone());
        Assert.assertEquals(frodo, sting.getAttachedTo());
    }

    @Test
    public void StingAbilityAvailableInBothFellowshipAndRegroup() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");

        scn.AttachCardsTo(frodo, sting);

        scn.StartGame();

        assertTrue(scn.FreepsCardActionAvailable(sting));

        scn.SkipToPhase(Phase.REGROUP);

        assertTrue(scn.FreepsCardActionAvailable(sting));
    }

    @Test
    public void StingAbilityExertsFrodoAndRevealsFourCards() throws DecisionResultInvalidException, CardNotFoundException {
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");

        PhysicalCardImpl orc1 = scn.GetShadowCard("orc1");
        PhysicalCardImpl orc2 = scn.GetShadowCard("orc2");
        PhysicalCardImpl orc3 = scn.GetShadowCard("orc3");
        PhysicalCardImpl scimitar1 = scn.GetShadowCard("scimitar1");

        scn.AttachCardsTo(frodo, sting);

        scn.ShadowMoveCardToHand(orc1, orc2, orc3, scimitar1);

        scn.StartGame();

        scn.FreepsUseCardAction(sting);

        //Reveals 2 cards
        assertEquals(4, scn.FreepsGetADParamAsList("blueprintId").size());
        assertEquals(1, scn.GetWoundsOn(frodo));
    }

    @Test
    public void StingAbilityRemovesTwilight() throws DecisionResultInvalidException, CardNotFoundException {
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl merry = scn.GetFreepsCard("merry");

        PhysicalCardImpl orc1 = scn.GetShadowCard("orc1");

        scn.FreepsMoveCharToTable(sam, merry);
        scn.AttachCardsTo(frodo, sting);

        scn.ShadowMoveCardToHand(orc1);

        scn.StartGame();

        scn.SetTwilight(10);

        scn.FreepsUseCardAction(sting);
        //Both players need to dismiss the card reveal dialog.
        scn.FreepsSkipCurrentPhaseAction();
        scn.ShadowSkipCurrentPhaseAction();

        // 10 twilight - 1 orc in the hand = 9 twilight
        assertEquals(9, scn.GetTwilight());

        scn.FreepsUseCardAction(sting);
        scn.FreepsSkipCurrentPhaseAction();
        scn.ShadowSkipCurrentPhaseAction();
        // 9 twilight - 1 orc in the hand = 8 twilight
        assertEquals(8, scn.GetTwilight());

        scn.FreepsUseCardAction(sting);
        scn.FreepsSkipCurrentPhaseAction();
        scn.ShadowSkipCurrentPhaseAction();
        // limit of 2 should have been hit, so no twilight should be removed
        assertEquals(8, scn.GetTwilight());

    }

}
