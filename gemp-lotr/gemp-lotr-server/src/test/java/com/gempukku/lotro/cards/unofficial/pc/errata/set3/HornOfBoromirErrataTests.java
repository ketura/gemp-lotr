package com.gempukku.lotro.cards.unofficial.pc.errata.set3;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class HornOfBoromirErrataTests
{
    protected GenericCardTestHelper GetFOTRScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("horn", "53_42");
                    put("elrond", "1_40");
                    put("boromir", "1_97");
                    put("aragorn", "1_89");

                    put("runner1", "1_178");
                }},
                GenericCardTestHelper.FellowshipSites,
                GenericCardTestHelper.FOTRFrodo,
                GenericCardTestHelper.FOTRRing
        );
    }

    protected GenericCardTestHelper GetMovieScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("horn", "53_42");
                    put("elrond", "1_40");
                    put("boromir", "1_97");
                    put("aragorn", "1_89");

                    put("runner1", "1_178");
                    put("runner2", "1_178");
                }}
        );
    }

    @Test
    public void HornStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 1E
         * Title: *Horn of Boromir
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 1
         * Type: Possession
         * Errata Game Text: Bearer must be Boromir.
         * Maneuver: Exert Boromir and discard this possession to spot an ally.  Until the regroup phase, that ally is strength +3 and participates in archery fire and skirmishes.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetFOTRScenario();

        PhysicalCardImpl horn = scn.GetFreepsCard("horn");

        assertTrue(horn.getBlueprint().isUnique());
        assertEquals(1, horn.getBlueprint().getTwilightCost());
    }

    @Test
    public void CanBeBorneByBoromir() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetFOTRScenario();

        PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl horn = scn.GetFreepsCard("horn");

        scn.FreepsMoveCharToTable(elrond);

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(horn);
        scn.FreepsMoveCardToHand(boromir);

        scn.StartGame();

        assertFalse(scn.FreepsCardPlayAvailable(horn));
        scn.FreepsPlayCard(boromir);
        assertTrue(scn.FreepsCardPlayAvailable(horn));
        scn.FreepsPlayCard(horn);

        Assert.assertTrue(scn.IsAttachedTo(horn, boromir));
    }

    @Test
    public void AbilityExertsAndDiscardsToPermitAllyToSkirmish() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetFOTRScenario();

        PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl horn = scn.GetFreepsCard("horn");
        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");

        scn.FreepsMoveCharToTable(elrond);
        scn.FreepsMoveCharToTable(boromir);
        scn.FreepsMoveCardToHand(horn);

        scn.ShadowMoveCharToTable(runner1);

        scn.StartGame();

        scn.FreepsPlayCard(horn);

        scn.SkipToPhase(Phase.MANEUVER);
        assertTrue(scn.FreepsCardActionAvailable(horn));
        scn.FreepsUseCardAction(horn);

        //discards
        assertFalse(scn.IsAttachedTo(horn, boromir));
        assertEquals(1, scn.GetWoundsOn(boromir));
        assertEquals(11, scn.GetStrength(elrond));

        scn.SkipToPhase(Phase.ASSIGNMENT);

        scn.SkipCurrentPhaseActions();

        scn.FreepsAssignToMinions(elrond, runner1);
        assertTrue(scn.IsCharAssigned(elrond));
        assertEquals(11, scn.GetStrength(elrond));
        
    }


}
