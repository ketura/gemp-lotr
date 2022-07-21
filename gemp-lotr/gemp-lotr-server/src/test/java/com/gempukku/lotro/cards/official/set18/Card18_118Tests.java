package com.gempukku.lotro.cards.official.set18;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Wielder of the Flame
public class Card18_118Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("aragorn", "1_89");

                    put("lurtz", "18_118");

                }}
        );
    }

    @Test
    public void LurtzAbilityOffersChoiceOfPrevention() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        scn.FreepsMoveCharToTable(aragorn);

        PhysicalCardImpl lurtz = scn.GetShadowCard("lurtz");
        scn.ShadowMoveCharToTable(lurtz);

        scn.StartGame();

        scn.SkipToPhase(Phase.ARCHERY);
        scn.PassCurrentPhaseActions();
        scn.FreepsChooseCard(frodo);

        //assignment phase
        scn.FreepsPassCurrentPhaseAction();
        assertEquals(0, scn.GetWoundsOn(lurtz));

        scn.ShadowUseCardAction(lurtz);
        assertEquals(2, scn.GetWoundsOn(lurtz));
        assertTrue(scn.FreepsAnyDecisionsAvailable());
        scn.FreepsAcceptOptionalTrigger();
        assertTrue(scn.FreepsAnyDecisionsAvailable());
        assertEquals("Exert - Aragorn, Ranger of the North", scn.FreepsGetADParamAsList("results").toArray()[0]);
        assertEquals("Add 1 burden", scn.FreepsGetADParamAsList("results").toArray()[1]);
    }



}