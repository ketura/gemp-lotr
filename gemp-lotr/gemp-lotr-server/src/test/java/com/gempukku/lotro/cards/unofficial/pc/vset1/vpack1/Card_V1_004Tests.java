package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_004Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("aragorn", "1_89");
                    put("boromir", "1_97");
                    put("loud", "101_4");

                    put("attea", "1_229");
                }}
        );
    }


    @Test
    public void LoudAndClearStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: *Loud and Clear It Sounds
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 1
         * Type: Condition
         * Subtype: Support Area
         * Game Text: Each time the Shadow player assigns as minion to a [shire] companion, you may exert a [gondor] companion and discard this condition to assign that minion to that [gondor] companion instead.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl loud = scn.GetFreepsCard("loud");

        assertTrue(scn.HasKeyword(loud, Keyword.SUPPORT_AREA));
        assertEquals(1, loud.getBlueprint().getTwilightCost());
        assertTrue(loud.getBlueprint().isUnique());
    }

    @Test
    public void LoudAndClearInterceptsShadowAssignment() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl loud = scn.GetFreepsCard("loud");

        scn.FreepsMoveCharToTable(boromir, aragorn);
        scn.FreepsMoveCardToSupportArea(loud);

        PhysicalCardImpl attea = scn.GetShadowCard("attea");
        scn.ShadowMoveCharToTable(attea);

        scn.StartGame();
        scn.SkipToPhase(Phase.ASSIGNMENT);

        //Assignment actions
        scn.SkipCurrentPhaseActions();

        //Skip standard assignment so shadow assigns
        scn.FreepsSkipCurrentPhaseAction();

        scn.ShadowAssignToMinions(frodo, attea);

        assertTrue(scn.FreepsHasOptionalTriggerAvailable());
        scn.FreepsAcceptOptionalTrigger();
        scn.FreepsChooseCard(boromir);

        assertTrue(loud.getZone() == Zone.DISCARD);
        assertTrue(scn.IsCharAssigned(boromir));
        assertEquals(1, scn.GetWoundsOn(boromir));
        assertFalse(scn.IsCharAssigned(frodo));

        scn.FreepsResolveSkirmish(boromir);
        scn.SkipCurrentPhaseActions();

        //fierce skirmish
        scn.SkipCurrentPhaseActions();
        scn.FreepsAssignToMinions(frodo, attea);
        //should only trigger if shadow assigns
        assertFalse(scn.FreepsHasOptionalTriggerAvailable());
    }


}
