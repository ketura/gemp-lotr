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

public class Saruman_KoIErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("legolas", "1_50");
                    put("tale", "1_66");
                    put("doubleshot", "1_38");

                    put("saruman", "51_3068");
                    put("uruk1", "1_151");
                    put("uruk2", "1_151");
                }}
        );
    }


    @Test
    public void SarumanWorks() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl legolas = scn.GetFreepsCard("legolas");
        PhysicalCardImpl tale = scn.GetFreepsCard("tale");
        PhysicalCardImpl doubleshot = scn.GetFreepsCard("doubleshot");

        PhysicalCardImpl saruman = scn.GetShadowCard("saruman");
        PhysicalCardImpl uruk1 = scn.GetShadowCard("uruk1");
        PhysicalCardImpl uruk2 = scn.GetShadowCard("uruk2");

        scn.FreepsMoveCharToTable(legolas);
        scn.FreepsMoveCardToHand(tale);
        scn.FreepsMoveCardToHand(doubleshot);

        scn.ShadowMoveCharToTable(saruman);
        scn.ShadowMoveCharToTable(uruk1);
        scn.ShadowMoveCharToTable(uruk2);


        scn.StartGame();

        scn.FreepsPlayCard(tale);

        scn.SkipToPhase(Phase.ARCHERY);

        //can't hit saruman
        scn.FreepsUseCardAction(legolas);
        assertFalse(scn.FreepsCanChooseCharacter(saruman));

        scn.FreepsChooseCard(uruk1);
        assertTrue(scn.ShadowCardActionAvailable(saruman));

        //saruman blocks the uruk hit
        scn.ShadowUseCardAction(saruman);

        assertEquals(0, scn.GetWoundsOn(uruk1));
        assertEquals(1, scn.GetWoundsOn(saruman));
        assertTrue(scn.HasKeyword(uruk1, Keyword.FIERCE));

        //shadow has to skip archery actions
        scn.ShadowSkipCurrentPhaseAction();

        //Repeat the greenleaf ability to ensure the last can't be blocked by saruman
        scn.FreepsUseCardAction(legolas);
        scn.FreepsChooseCard(uruk1);
        scn.ShadowUseCardAction(saruman);
        scn.ShadowSkipCurrentPhaseAction();

        scn.FreepsUseCardAction(legolas);
        scn.FreepsChooseCard(uruk1);
        scn.ShadowUseCardAction(saruman);
        scn.ShadowSkipCurrentPhaseAction();

        //greenleaf is out of vitality, so play doubleshot
        scn.FreepsPlayCard(doubleshot);
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsSkipCurrentPhaseAction();

        //Archery wound
        scn.ShadowChooseCard(uruk1);

        //Now that we've done 4 total wounds, we should see an exhausted saruman and an uruk1 with 1 wound
        assertEquals(1, scn.GetWoundsOn(uruk1));
        assertEquals(3, scn.GetWoundsOn(saruman));


        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.SkipCurrentPhaseActions();

        //saruman is not on the list of assignable minions
        assertEquals(2, scn.FreepsGetADParamAsList("minions").size());

    }
}
