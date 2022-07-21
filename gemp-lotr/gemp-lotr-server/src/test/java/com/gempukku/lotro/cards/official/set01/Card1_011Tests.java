package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

// Tidings of Erebor
public class Card1_011Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("farin", "1_11");

                    put("goblin", "1_178");
                }}
        );
    }

    @Test
    public void FarinStrengthBoostWhileSkirmishingOrc() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl farin = scn.GetFreepsCard("farin");
        scn.FreepsMoveCharToTable(farin);

        PhysicalCardImpl goblin = scn.GetShadowCard("goblin");
        scn.ShadowMoveCharToTable(goblin);

        scn.StartGame();
        scn.FreepsPassCurrentPhaseAction();

        scn.SkipToPhase(Phase.ASSIGNMENT);

        scn.PassCurrentPhaseActions();

        scn.FreepsAssignToMinions(farin, goblin);
        scn.FreepsResolveSkirmish(farin);

        assertEquals(7, scn.GetStrength(farin));

    }



}