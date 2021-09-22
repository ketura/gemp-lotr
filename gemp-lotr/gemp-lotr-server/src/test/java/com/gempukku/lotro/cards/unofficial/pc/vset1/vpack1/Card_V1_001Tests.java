package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_001Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("gimli", "1_13");
                    put("farin", "1_11");
                    put("hosp", "151_1");
                    put("handaxe1", "2_10");
                    put("handaxe2", "2_10");
                    put("handaxe3", "2_10");
                }},
                GenericCardTestHelper.FellowshipSites,
                GenericCardTestHelper.FOTRFrodo,
                GenericCardTestHelper.FOTRRing
        );
    }


    @Test
    public void HospitalityStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: *Hospitality of the Dwarves
         * Side: Free Peoples
         * Culture: Dwarven
         * Twilight Cost: 1
         * Type: Condition
         * Subtype: Support Area
         * Game Text: At sites 4 through 8, each [dwarf] companion bearing more than 1 possession is strength +1.
         * Each time the fellowship moves you may spot a [dwarf] companion bearing more than one possession to draw a card.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl hosp = scn.GetFreepsCard("hosp");

        assertTrue(scn.HasKeyword(hosp, Keyword.SUPPORT_AREA));
        assertEquals(1, hosp.getBlueprint().getTwilightCost());
        assertTrue(hosp.getBlueprint().isUnique());
    }

    @Test
    public void StrengthBuffWhenAt4To8AndBearing2PlusItems() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl farin = scn.GetFreepsCard("farin");
        PhysicalCardImpl hosp = scn.GetFreepsCard("hosp");

        PhysicalCardImpl handaxe1 = scn.GetFreepsCard("handaxe1");
        PhysicalCardImpl handaxe2 = scn.GetFreepsCard("handaxe2");
        PhysicalCardImpl handaxe3 = scn.GetFreepsCard("handaxe3");

        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCharToTable(farin);
        scn.FreepsMoveCardToHand(hosp);
        scn.FreepsMoveCardToHand(handaxe1);
        scn.FreepsMoveCardToHand(handaxe2);
        scn.FreepsMoveCardToHand(handaxe3);

        scn.StartGame();
        scn.FreepsPlayCard(hosp);

        // Putting two hand axes on Gimli and one on Farin
        scn.FreepsPlayCard(handaxe1);
        scn.FreepsChooseCard(gimli);
        scn.FreepsPlayCard(handaxe2);
        scn.FreepsChooseCard(gimli);
        scn.FreepsPlayCard(handaxe3);
        //last axe goes on Farin automatically

        // 6 base strength + 1 axe + 1 axe
        assertEquals(8, scn.GetStrength(gimli));
        // 5 base strength + 1 axe
        assertEquals(6, scn.GetStrength(farin));

        //Cheat our way to unlimited moves
        scn.ApplyAdHocModifier(new MoveLimitModifier(null, 8));

        // Move to site 2
        scn.SkipToPhase(Phase.REGROUP);

        // Move to site 3
        scn.SkipCurrentPhaseActions();
        scn.FreepsChooseToMove();
        //THe other effect of Hospitality can be ignored
        scn.FreepsAcceptOptionalTrigger();

        assertEquals(8, scn.GetStrength(gimli));
        assertEquals(6, scn.GetStrength(farin));


        // Move to site 4
        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsChooseToMove();
        scn.FreepsAcceptOptionalTrigger();

        // 6 base strength + 1 axe + 1 axe + 1 Hospitality
        assertEquals(9, scn.GetStrength(gimli));
        // 6 base strength + 1 axe + 1 axe + 0 Hospitality
        assertEquals(6, scn.GetStrength(farin));

        // Move to site 5
        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsChooseToMove();
        scn.FreepsAcceptOptionalTrigger();

        assertEquals(9, scn.GetStrength(gimli));
        assertEquals(6, scn.GetStrength(farin));

        // Move to site 6
        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsChooseToMove();
        scn.FreepsAcceptOptionalTrigger();

        assertEquals(9, scn.GetStrength(gimli));
        assertEquals(6, scn.GetStrength(farin));

        // Move to site 7
        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsChooseToMove();
        scn.FreepsAcceptOptionalTrigger();

        assertEquals(9, scn.GetStrength(gimli));
        assertEquals(6, scn.GetStrength(farin));

        // Move to site 8
        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsChooseToMove();
        scn.FreepsAcceptOptionalTrigger();

        assertEquals(9, scn.GetStrength(gimli));
        assertEquals(6, scn.GetStrength(farin));

        // Move to site 9
        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsChooseToMove();
        scn.FreepsAcceptOptionalTrigger();

        //lose the hospitality
        assertEquals(8, scn.GetStrength(gimli));
        assertEquals(6, scn.GetStrength(farin));
    }

    @Test
    public void DrawsCardWhenBearing2PlusItems() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl hosp = scn.GetFreepsCard("hosp");

        PhysicalCardImpl handaxe1 = scn.GetFreepsCard("handaxe1");
        PhysicalCardImpl handaxe2 = scn.GetFreepsCard("handaxe2");

        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(hosp);
        scn.FreepsMoveCardToHand(handaxe1);
        scn.FreepsMoveCardToHand(handaxe2);

        scn.StartGame();
        scn.FreepsPlayCard(hosp);

        // Putting two hand axes on Gimli
        scn.FreepsPlayCard(handaxe1);
        scn.FreepsPlayCard(handaxe2);

        //one farin, one axe in the deck
        assertEquals(2, scn.GetFreepsDeckCount());
        assertEquals(0, scn.GetFreepsHandCount());

        scn.FreepsSkipCurrentPhaseAction();
        scn.FreepsAcceptOptionalTrigger();

        assertEquals(1, scn.GetFreepsDeckCount());
        assertEquals(1, scn.GetFreepsHandCount());

        scn.SkipToPhase(Phase.REGROUP);

        scn.FreepsMoveCardToDiscard(handaxe1);
        assertEquals(1, scn.GetAttachedCards(gimli).size());

        // Move to site 3
        scn.SkipCurrentPhaseActions();
        scn.FreepsChooseToMove();

        //Should not have drawn a card with only 1 possession on gimli
        assertEquals(1, scn.GetFreepsDeckCount());
        assertEquals(1, scn.GetFreepsHandCount());
        assertFalse(scn.FreepsAnyDecisionsAvailable());

    }
}
