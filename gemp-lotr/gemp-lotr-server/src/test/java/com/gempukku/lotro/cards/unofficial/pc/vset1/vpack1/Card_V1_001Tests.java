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

public class Card_V1_001Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("aragorn", "1_89");
                    put("boromir", "1_97");
                    put("lords", "101_1");

                    put("runner1", "1_178");
                    put("runner2", "1_178");
                }}
        );
    }


    @Test
    public void LordsStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: The Lords of Gondor Have Returned
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 2
         * Type: Condition
         * Subtype: Support Area
         * Game Text: While Boromir is assigned to skirmish more than one minion, Aragorn is strength +3.
         * While Aragorn is assigned to skirmish more than one minion, Boromir is strength +3.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl lords = scn.GetFreepsCard("lords");

        assertTrue(scn.HasKeyword(lords, Keyword.SUPPORT_AREA));
        assertEquals(2, lords.getBlueprint().getTwilightCost());
        assertTrue(lords.getBlueprint().isUnique());
    }

//    @Test
//    public void OneIsBuffedWhenOtherAssignedMultiple() throws DecisionResultInvalidException, CardNotFoundException {
//        //Pre-game setup
//        GenericCardTestHelper scn = GetScenario();
//
//        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
//        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
//        PhysicalCardImpl lords = scn.GetFreepsCard("lords");
//
//        scn.FreepsMoveCharToTable(aragorn);
//        scn.FreepsMoveCharToTable(boromir);
//        scn.FreepsMoveCardToHand(lords);
//
//        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
//        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");
//
//        scn.ShadowMoveCharToTable(runner1);
//        scn.ShadowMoveCharToTable(runner2);
//
//        scn.StartGame();
//        scn.FreepsPlayCard(lords);
//        scn.SkipToPhase(Phase.ASSIGNMENT);
//
//        assertEquals(7, scn.GetStrength(boromir));
//        assertEquals(8, scn.GetStrength(aragorn));
//
//        scn.FreepsSkipCurrentPhaseAction();
//        scn.ShadowSkipCurrentPhaseAction();
//
//        //Let shadow assign so we don't have to worry about defender bonuses
//        scn.FreepsSkipCurrentPhaseAction();
//        scn.ShadowAssignToMinions(boromir, runner1, runner2);
//
//        assertEquals(7, scn.GetStrength(boromir));
//        assertEquals(11, scn.GetStrength(aragorn));
//
//        scn.FreepsResolveSkirmish(boromir);
//
//        scn.SkipToPhase(Phase.REGROUP);
//        scn.SkipCurrentPhaseActions();
//        scn.ShadowSkipCurrentPhaseAction();
//        scn.FreepsChooseToMove();
//
//        scn.SkipToPhase(Phase.ASSIGNMENT);
//
//        //Let shadow assign so we don't have to worry about defender bonuses
//        scn.FreepsSkipCurrentPhaseAction();
//        scn.ShadowAssignToMinions(aragorn, runner1, runner2);
//
//        assertEquals(10, scn.GetStrength(boromir));
//        assertEquals(8, scn.GetStrength(aragorn));
//    }


    @Test
    public void NoBuffsWhenAssignedSingle() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl lords = scn.GetFreepsCard("lords");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCharToTable(boromir);
        scn.FreepsMoveCardToHand(lords);

        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");

        scn.ShadowMoveCharToTable(runner1);
        scn.ShadowMoveCharToTable(runner2);

        scn.StartGame();
        scn.FreepsPlayCard(lords);
        scn.SkipToPhase(Phase.ASSIGNMENT);

        assertEquals(7, scn.GetStrength(boromir));
        assertEquals(8, scn.GetStrength(aragorn));

        scn.SkipCurrentPhaseActions();

        scn.FreepsAssignToMinions(new PhysicalCardImpl[]{ boromir, runner1 }, new PhysicalCardImpl[]{ aragorn, runner2 });

        assertEquals(7, scn.GetStrength(boromir));
        assertEquals(8, scn.GetStrength(aragorn));

    }
}
