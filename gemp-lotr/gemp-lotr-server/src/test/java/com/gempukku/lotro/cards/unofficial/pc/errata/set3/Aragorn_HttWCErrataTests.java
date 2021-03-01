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
        assertFalse(scn.HasKeyword(aragorn, Keyword.CONCEALED));
    }


    @Test
    public void AragornAbilityOnlyWorksDuringFellowship() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        scn.FreepsMoveCharToTable(aragorn, gimli);

        scn.StartGame();
        scn.FreepsSkipCurrentPhaseAction();

        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
        assertTrue(scn.FreepsActionAvailable("Optional"));
        scn.FreepsAcceptOptionalTrigger();
        scn.FreepsChooseCard(frodo);
        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));

        scn.SkipToPhase(Phase.REGROUP);

        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
        scn.FreepsChooseToMove();

        assertFalse(scn.FreepsActionAvailable("Optional"));
        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
    }

    @Test
    public void AragornAbilityTriggersBeforeConcealedTwilightIsRemoved() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        scn.FreepsMoveCardToHand(aragorn, gimli);

        scn.StartGame();
        scn.FreepsPlayCard(aragorn);
        scn.FreepsPlayCard(gimli);
        assertEquals(6, scn.GetTwilight());
        scn.FreepsSkipCurrentPhaseAction();

        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
        assertFalse(scn.HasKeyword(aragorn, Keyword.CONCEALED));
        assertTrue(scn.FreepsActionAvailable("Optional"));
        scn.FreepsAcceptOptionalTrigger();
        scn.FreepsChooseCard(frodo);
        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));
        assertTrue(scn.HasKeyword(aragorn, Keyword.CONCEALED));
        //6 from playing aragorn/gimli, 1+1+1 for companions, 1 for the site (King's Tent), -2 for concealed
        assertEquals(8, scn.GetTwilight());
    }

    @Test
    public void AragornAbilityOffersChoiceOfPrevention() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");

        PhysicalCardImpl minion = scn.GetShadowCard("minion");
        PhysicalCardImpl tracker = scn.GetShadowCard("tracker");

        scn.FreepsMoveCharToTable(aragorn);
        scn.ShadowMoveCardToHand(minion, tracker);

        scn.StartGame();
        scn.FreepsSkipCurrentPhaseAction();

        assertTrue(scn.FreepsActionAvailable("Optional"));
        scn.FreepsAcceptOptionalTrigger();
        assertTrue(scn.ShadowDecisionAvailable("discard a minion (or reveal a tracker"));
        scn.ShadowAcceptOptionalTrigger();

        assertTrue(scn.ShadowAnyDecisionsAvailable());
        assertEquals("Discard a minion from hand", scn.ShadowGetADParamAsList("results").toArray()[0]);
        assertEquals("Reveal a tracker from hand", scn.ShadowGetADParamAsList("results").toArray()[1]);
    }

    @Test
    public void AragornAbilityCanBePreventedWithMinionDiscard() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");

        PhysicalCardImpl minion = scn.GetShadowCard("minion");

        scn.FreepsMoveCharToTable(aragorn);
        scn.ShadowMoveCardToHand(minion);

        scn.StartGame();
        scn.FreepsSkipCurrentPhaseAction();

        assertTrue(scn.FreepsActionAvailable("Optional"));
        scn.FreepsAcceptOptionalTrigger();
        assertTrue(scn.ShadowDecisionAvailable("discard a minion"));
        assertEquals(1, scn.GetShadowHandCount());
        assertEquals(0, scn.GetShadowDiscardCount());
        scn.ShadowAcceptOptionalTrigger();
        assertEquals(0, scn.GetShadowHandCount());
        assertEquals(1, scn.GetShadowDiscardCount());
        assertFalse(scn.FreepsAnyDecisionsAvailable());

        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
    }

    @Test
    public void AragornAbilityCanBePreventedWithTrackerReveal() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");

        PhysicalCardImpl tracker = scn.GetShadowCard("tracker");

        scn.FreepsMoveCharToTable(aragorn);
        scn.ShadowMoveCardToHand(tracker);

        scn.StartGame();
        scn.FreepsSkipCurrentPhaseAction();

        assertTrue(scn.FreepsActionAvailable("Optional"));
        scn.FreepsAcceptOptionalTrigger();
        assertTrue(scn.ShadowDecisionAvailable("reveal a tracker"));
        assertEquals(1, scn.GetShadowHandCount());
        scn.ShadowAcceptOptionalTrigger();
        assertEquals(1, scn.GetShadowHandCount());
        assertTrue(scn.ShadowDecisionAvailable("Choose action to perform"));
        scn.ShadowChooseMultipleChoiceOption("Reveal a tracker");

        assertFalse(scn.FreepsAnyDecisionsAvailable());
        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));

    }

}
