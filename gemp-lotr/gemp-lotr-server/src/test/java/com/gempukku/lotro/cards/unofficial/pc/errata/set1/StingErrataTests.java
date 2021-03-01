package com.gempukku.lotro.cards.unofficial.pc.errata.set1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StingErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("sting", "51_313");
                    put("sam", "1_311");
                    put("merry", "1_302");

                    put("orc1", "1_178");
                    put("orc2", "1_178");
                    put("orc3", "1_178");
                    put("scimitar1", "1_180");
                    put("scimitar2", "1_180");
                    put("scimitar3", "1_180");
                }}
        );
    }


    @Test
    public void StingCanOnlyBeBorneByFrodo() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");

        scn.FreepsMoveCharToTable(sam);
        scn.FreepsMoveCardToHand(sting);

        scn.StartGame();

        scn.FreepsPlayCard(sting);

        //should have automatically gone to frodo as the only valid target
        Assert.assertEquals(Zone.ATTACHED, sting.getZone());
        Assert.assertEquals(frodo, sting.getAttachedTo());
    }

    @Test
    public void StingAbilityAvailableInBothFellowshipAndRegroup() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");

        scn.AttachCard(sting, frodo);

        scn.StartGame();

        assertTrue(scn.FreepsCardActionAvailable(sting));

        scn.SkipToPhase(Phase.REGROUP);

        assertTrue(scn.FreepsCardActionAvailable(sting));
    }

    @Test
    public void StingAbilityExertsFrodoAndRevealsThreeCards() throws DecisionResultInvalidException, CardNotFoundException {
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");

        PhysicalCardImpl orc1 = scn.GetShadowCard("orc1");
        PhysicalCardImpl orc2 = scn.GetShadowCard("orc2");
        PhysicalCardImpl orc3 = scn.GetShadowCard("orc3");
        PhysicalCardImpl scimitar1 = scn.GetShadowCard("scimitar1");

        scn.AttachCard(sting, frodo);

        scn.ShadowMoveCardToHand(orc1, orc2, orc3, scimitar1);

        scn.StartGame();

        scn.FreepsUseCardAction(sting);

        //Reveals 3 cards
        assertEquals(3, scn.FreepsGetADParamAsList("blueprintId").size());
        assertEquals(1, scn.GetWoundsOn(frodo));
    }

    @Test
    public void StingAbilityMakesConcealedIfOrcRevealed() throws DecisionResultInvalidException, CardNotFoundException {
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl merry = scn.GetFreepsCard("merry");

        PhysicalCardImpl orc1 = scn.GetShadowCard("orc1");

        scn.FreepsMoveCharToTable(sam, merry);
        scn.AttachCard(sting, frodo);

        scn.ShadowMoveCardToHand(orc1);

        scn.StartGame();

        scn.FreepsUseCardAction(sting);

        //Both players have revealed cards as an action to dismiss
        scn.SkipCurrentPhaseActions();

        assertTrue(scn.FreepsCanChooseCharacter(frodo));
        assertTrue(scn.FreepsCanChooseCharacter(sam));
        assertTrue(scn.FreepsCanChooseCharacter(merry));

        scn.FreepsChooseCard(frodo);
        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));

        scn.FreepsUseCardAction(sting);
        scn.SkipCurrentPhaseActions();
        assertFalse(scn.FreepsCanChooseCharacter(frodo));
        assertTrue(scn.FreepsCanChooseCharacter(sam));
        assertTrue(scn.FreepsCanChooseCharacter(merry));

        scn.FreepsChooseCard(sam);
        assertTrue(scn.HasKeyword(sam, Keyword.CONCEALED));
    }


    @Test
    public void StingConcealedLastsUntilNextRegroup() throws DecisionResultInvalidException, CardNotFoundException {
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl sting = scn.GetFreepsCard("sting");
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");

        PhysicalCardImpl orc1 = scn.GetShadowCard("orc1");

        scn.FreepsMoveCharToTable(sam);
        scn.AttachCard(sting, frodo);

        scn.ShadowMoveCardToHand(orc1);

        scn.StartGame();

        scn.SkipToPhase(Phase.REGROUP);

        scn.FreepsUseCardAction(sting);
        scn.FreepsChooseCard(frodo);
        //Both players have revealed cards as an action to dismiss
        scn.SkipCurrentPhaseActions();

        scn.FreepsChooseCard(frodo);
        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));

        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsSkipCurrentPhaseAction();
        //shadow reconcile
        scn.ShadowSkipCurrentPhaseAction();

        scn.FreepsChooseToMove();

        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));
    }
}
