package com.gempukku.lotro.cards.official.set02;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

// Wielder of the Flame
public class Card2_028Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("wielder", "2_28");
                    put("gandalf", "1_364");

                    put("runner", "1_178");

                }}
        );
    }

    @Test
    public void WielderAbilityCanBePrevented() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl wielder = scn.GetFreepsCard("wielder");
        PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
        scn.FreepsMoveCharToTable(gandalf);
        scn.FreepsMoveCardToHand(wielder);

        PhysicalCardImpl runner = scn.GetShadowCard("runner");
        scn.ShadowMoveCharToTable(runner);

        scn.StartGame();
        scn.FreepsSkipCurrentPhaseAction();

        scn.SkipToPhase(Phase.MANEUVER);


        scn.FreepsPlayCard(wielder);
        assertEquals(4, scn.GetTwilight());
        scn.ShadowAcceptOptionalTrigger();
        assertEquals(1, scn.GetTwilight());
        assertFalse(scn.FreepsAnyActionsAvailable());

    }



}