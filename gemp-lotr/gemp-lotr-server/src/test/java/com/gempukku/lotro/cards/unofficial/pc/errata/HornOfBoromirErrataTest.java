package com.gempukku.lotro.cards.unofficial.pc.errata;

import com.gempukku.lotro.cards.GenericCardTest;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class HornOfBoromirErrataTest
{
    protected GenericCardTest GetFOTRScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("horn", "21_3042");
                    put("elrond", "1_40");
                    put("boromir", "1_97");
                    put("aragorn", "1_89");

                    put("runner1", "1_178");
                    put("runner2", "1_178");
                }},
                GenericCardTest.FellowshipSites,
                GenericCardTest.FOTRFrodo,
                GenericCardTest.FOTRRing
        );
    }

    protected GenericCardTest GetMovieScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("horn", "21_3042");
                    put("elrond", "1_40");
                    put("boromir", "1_97");
                    put("aragorn", "1_89");

                    put("runner1", "1_178");
                    put("runner2", "1_178");
                }}
        );
    }

    @Test
    public void CanBeBorneByBoromir() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetFOTRScenario();

        PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl horn = scn.GetFreepsCard("horn");

        scn.FreepsMoveCharToTable(elrond);

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(horn);
        scn.FreepsMoveCardToHand(boromir);

        scn.StartGame();

        assertFalse(scn.FreepsActionAvailable("Play Horn of Boromir"));
        scn.FreepsUseAction("Play Boromir");
        assertTrue(scn.FreepsActionAvailable("Play Horn of Boromir"));
        scn.FreepsUseAction("Play Horn of Boromir");

        Assert.assertEquals(Zone.ATTACHED, horn.getZone());
        Assert.assertEquals(boromir, horn.getAttachedTo());
    }

    @Test
    public void AbilityExertsAndAssignsBoromir() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetFOTRScenario();

        PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl horn = scn.GetFreepsCard("horn");
        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");

        scn.FreepsMoveCharToTable(elrond);
        scn.FreepsMoveCharToTable(boromir);
        scn.FreepsMoveCardToHand(horn);

        scn.ShadowMoveCharToTable(runner1);
        scn.ShadowMoveCharToTable(runner2);

        scn.StartGame();

        scn.FreepsUseAction("Play Horn of Boromir");

        scn.SkipToPhase(Phase.ASSIGNMENT);
        assertTrue(scn.FreepsActionAvailable("Use Horn of Boromir"));
        scn.FreepsUseAction("Use Horn of Boromir");
        scn.FreepsChooseCard(runner1);

        assertEquals(1, scn.GetWoundsOn(boromir));
        assertEquals(11, scn.GetStrength(elrond));
        //Skip shadow player's action
        scn.SkipCurrentPhaseActions();
        assertFalse(scn.FreepsActionAvailable("Use Horn of Boromir"));
        scn.SkipCurrentPhaseActions();
        assertTrue(scn.IsCharAssigned(boromir));

        scn.FreepsAssignToMinion(elrond, runner2);
        assertTrue(scn.IsCharAssigned(elrond));
    }

    @Test
    public void AbilityDoesNotPumpFarAwayAllies() throws DecisionResultInvalidException, CardNotFoundException {
        GenericCardTest scn = GetMovieScenario();

        PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl horn = scn.GetFreepsCard("horn");
        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");

        scn.FreepsMoveCharToTable(elrond);
        scn.FreepsMoveCharToTable(boromir);
        scn.FreepsMoveCardToHand(horn);

        scn.ShadowMoveCharToTable(runner1);
        scn.ShadowMoveCharToTable(runner2);

        scn.StartGame();

        scn.FreepsUseAction("Play Horn of Boromir");

        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.FreepsUseAction("Use Horn of Boromir");
        scn.FreepsChooseCard(runner1);

        assertEquals(1, scn.GetWoundsOn(boromir));
        assertEquals(8, scn.GetStrength(elrond));
        //Skip shadow player's action
        scn.SkipCurrentPhaseActions();

    }
}
