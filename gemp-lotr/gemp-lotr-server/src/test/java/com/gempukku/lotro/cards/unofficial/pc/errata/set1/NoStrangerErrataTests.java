package com.gempukku.lotro.cards.unofficial.pc.errata.set1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoStrangerErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("aragorn", "1_89");
                    put("arwen", "1_30");
                    put("boromir", "1_97");
                    put("nostranger", "51_10108");
                    put("nostranger2", "51_10108");
                }}
        );
    }


    @Test
    public void NoStrangerHasStealth() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");

        assertTrue(scn.HasKeyword(nostranger, Keyword.STEALTH));
    }

    @Test
    public void NoStrangerCanOnlyPlayOnRanger() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCharToTable(arwen);
        scn.FreepsMoveCharToTable(boromir);
        scn.FreepsMoveCardToHand(nostranger);

        scn.StartGame();

        assertTrue(scn.FreepsCardPlayAvailable(nostranger));

        scn.FreepsPlayCard(nostranger);

        //There are 3 companions in play, but only 2 rangers, so we should only see 2 options
        assertEquals(2, scn.FreepsGetADParamAsList("cardId").size());
    }

    @Test
    public void NoStrangerLimitOnePerBearer() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");
        PhysicalCardImpl nostranger2 = scn.GetFreepsCard("nostranger2");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCharToTable(arwen);
        scn.FreepsMoveCardToHand(nostranger);
        scn.FreepsMoveCardToHand(nostranger2);

        scn.StartGame();

        scn.FreepsPlayCard(nostranger);
        scn.FreepsChooseCard(aragorn);
        scn.FreepsPlayCard(nostranger2);

        //No prompt means it was placed automatically on Arwen, with no choice between Arwen and Aragorn
        assertFalse(scn.FreepsAnyActionsAvailable());

    }

    @Test
    public void NoStrangerAddsConcealed() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(nostranger);

        scn.StartGame();

        scn.FreepsPlayCard(nostranger);

        scn.HasKeyword(aragorn, Keyword.CONCEALED);
    }
}
