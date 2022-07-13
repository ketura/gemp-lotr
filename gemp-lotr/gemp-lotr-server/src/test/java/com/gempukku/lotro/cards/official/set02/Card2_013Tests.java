package com.gempukku.lotro.cards.official.set02;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Tidings of Erebor
public class Card2_013Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("tidings", "2_13");
                    put("gimli", "1_13");

                    put("deckcard1", "1_13");
                    put("deckcard2", "1_13");
                    put("deckcard3", "1_13");

                }}
        );
    }

    @Test
    public void TidingsAbilityCanBePrevented() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl tidings = scn.GetFreepsCard("tidings");
        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(tidings);

        scn.StartGame();
        scn.FreepsSkipCurrentPhaseAction();

        scn.SkipToPhase(Phase.REGROUP);


        scn.FreepsPlayCard(tidings);
        assertEquals(3, scn.GetTwilight());
        assertEquals(0, scn.GetFreepsHandCount());
        assertEquals(3, scn.GetFreepsDeckCount());
        scn.ShadowAcceptOptionalTrigger();
        assertEquals(0, scn.GetTwilight());
        assertEquals(0, scn.GetFreepsHandCount());
        assertEquals(3, scn.GetFreepsDeckCount());

    }



}