package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Card_V1_002Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("aragorn", "1_89");
                    put("boromir", "1_97");
                    put("whitecity", "101_2");

                    put("attea", "1_229");
                    put("cantea", "1_230");
                }}
        );
    }


    @Test
    public void WhiteCityStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: *I Will Not Let the White City Fall
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 2
         * Type: Condition
         * Subtype: Support Area
         * Game Text: Each time Boromir loses a skirmish, make Aragorn strength +2 until the regroup phase.
         * Each time Aragorn loses a skirmish, make Boromir strength +2 until the regroup phase.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl whitecity = scn.GetFreepsCard("whitecity");

        assertTrue(scn.HasKeyword(whitecity, Keyword.SUPPORT_AREA));
        assertEquals(2, whitecity.getBlueprint().getTwilightCost());
        assertTrue(whitecity.getBlueprint().isUnique());
    }

    @Test
    public void OneIsBuffedWhenOtherLosesSkirmish() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl whitecity = scn.GetFreepsCard("whitecity");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCharToTable(boromir);
        scn.FreepsMoveCardToHand(whitecity);

        PhysicalCardImpl cantea = scn.GetShadowCard("cantea");
        PhysicalCardImpl attea = scn.GetShadowCard("attea");
        scn.ShadowMoveCharToTable(cantea, attea);

        scn.StartGame();
        scn.FreepsPlayCard(whitecity);
        scn.SkipToPhase(Phase.ASSIGNMENT);

        assertEquals(7, scn.GetStrength(boromir));
        assertEquals(8, scn.GetStrength(aragorn));

        scn.SkipCurrentPhaseActions();

        //Standard assignment
        scn.FreepsAssignToMinions(new PhysicalCardImpl[] { aragorn, cantea}, new PhysicalCardImpl[] { boromir, attea});

        scn.FreepsResolveSkirmish(aragorn);
        scn.SkipCurrentPhaseActions();
        scn.FreepsAcceptOptionalTrigger(); //Have to resolve the skirmish and the condition trigger
        // 14 > 8, Aragorn lost, Boromir should be buffed
        assertEquals(9, scn.GetStrength(boromir));
        assertEquals(8, scn.GetStrength(aragorn));

        scn.FreepsResolveSkirmish(boromir);
        scn.SkipCurrentPhaseActions();
        scn.FreepsAcceptOptionalTrigger(); //Have to resolve the skirmish and the condition trigger
        // 12 > 9, Boromir lost, Aragorn should be buffed
        assertEquals(9, scn.GetStrength(boromir));
        assertEquals(10, scn.GetStrength(aragorn));

        //Fierce assignment
        scn.FreepsSkipCurrentPhaseAction();
        scn.ShadowSkipCurrentPhaseAction();

        scn.FreepsAssignToMinions(new PhysicalCardImpl[] { aragorn, cantea}, new PhysicalCardImpl[] { boromir, attea});
        scn.FreepsResolveSkirmish(boromir);
        scn.SkipCurrentPhaseActions();
        scn.FreepsAcceptOptionalTrigger(); //Have to resolve the skirmish and the condition trigger
        // 10 > 9, Boromir lost again, Aragorn should be buffed again
        assertEquals(9, scn.GetStrength(boromir));
        assertEquals(12, scn.GetStrength(aragorn));

        scn.FreepsResolveSkirmish(aragorn);
        //Aragorn now wins his skirmish, so there's no loss trigger to evaluate

        scn.SkipToPhase(Phase.REGROUP);

        //strength should be back to normal
        assertEquals(7, scn.GetStrength(boromir));
        assertEquals(8, scn.GetStrength(aragorn));
    }


}
