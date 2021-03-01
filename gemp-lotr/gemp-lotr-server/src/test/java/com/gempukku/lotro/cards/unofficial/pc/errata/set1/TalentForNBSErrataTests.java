package com.gempukku.lotro.cards.unofficial.pc.errata.set1;

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

public class TalentForNBSErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("talent", "51_316");
                    put("sam", "1_311");
                    put("gaffer", "1_291");
                    put("boromir", "1_97");
                }}
        );
    }

    @Test
    public void TalentHasStealth() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl talent = scn.GetFreepsCard("talent");

        assertTrue(scn.HasKeyword(talent, Keyword.STEALTH));
    }


    @Test
    public void TalentExertsAHobbitOnPlay() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl gaffer = scn.GetFreepsCard("gaffer");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl talent = scn.GetFreepsCard("talent");

        scn.FreepsMoveCharToTable(sam, gaffer, boromir);
        scn.FreepsMoveCardToHand(talent);

        scn.StartGame();

        scn.FreepsPlayCard(talent);
        //Frodo, Sam, Gaffer, but not Boromir
        assertEquals(3, scn.FreepsCardChoiceCount());

        scn.FreepsChooseCard(frodo);
        assertEquals(1, scn.GetWoundsOn(frodo));
    }

    @Test
    public void TalentAbilityTriggersConcealedOnMove() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl gaffer = scn.GetFreepsCard("gaffer");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl talent = scn.GetFreepsCard("talent");

        scn.FreepsMoveCharToTable(sam, gaffer, boromir);
        scn.FreepsMoveCardToHand(talent);

        scn.StartGame();

        scn.FreepsPlayCard(talent);
        scn.FreepsChooseCard(frodo);
        scn.FreepsSkipCurrentPhaseAction();

        //Sam and Frodo, but not Gaffer or Boromir
        assertEquals(2, scn.FreepsCardChoiceCount());
        scn.FreepsChooseCard(frodo);
        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));

        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();

        scn.FreepsChooseToMove();
        //Sam and Frodo, but not Gaffer or Boromir
        assertEquals(2, scn.FreepsCardChoiceCount());
        scn.FreepsChooseCard(sam);
        assertEquals(Phase.SHADOW, scn.GetCurrentPhase());
        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
        assertTrue(scn.HasKeyword(sam, Keyword.CONCEALED));

    }

    @Test
    public void TalentOnlyTriggersWithTwoHobbitCompanions() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl gaffer = scn.GetFreepsCard("gaffer");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl talent = scn.GetFreepsCard("talent");

        scn.FreepsMoveCharToTable(gaffer, boromir);
        scn.FreepsMoveCardToHand(talent);

        scn.StartGame();

        scn.FreepsPlayCard(talent);
        scn.FreepsChooseCard(frodo);
        scn.FreepsSkipCurrentPhaseAction();

        //neither gaffer nor boromir should permit the move trigger to choose a companion
        assertEquals(Phase.SHADOW, scn.GetCurrentPhase());
        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
    }


}
