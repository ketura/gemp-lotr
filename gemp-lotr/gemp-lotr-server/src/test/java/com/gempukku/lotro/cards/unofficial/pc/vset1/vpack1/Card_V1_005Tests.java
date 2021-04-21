package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_005Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("aragorn", "1_89");
                    put("boromir", "1_97");
                    put("remnant", "101_5");

                    put("inquisitor", "1_268");
                }}
        );
    }


    @Test
    public void RemnantStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: *Remnant of Numenor
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 1
         * Type: Condition
         * Subtype: Support Area
         * Game Text: To play, spot a [gondor] companion.
         * Each time a card is discarded from your hand by a Shadow card, you may add (1) to shuffle a [gondor] card from your discard pile into your draw deck.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl remnant = scn.GetFreepsCard("remnant");

        assertTrue(scn.HasKeyword(remnant, Keyword.SUPPORT_AREA));
        assertEquals(1, remnant.getBlueprint().getTwilightCost());
    }

    @Test
    public void RemnantSpotsAGondorCompanionToPlay() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl remnant = scn.GetFreepsCard("remnant");

        scn.FreepsMoveCardToHand(aragorn, remnant);

        scn.StartGame();

        assertEquals(Phase.FELLOWSHIP, scn.GetCurrentPhase());
        Assert.assertFalse(scn.FreepsCardPlayAvailable(remnant));

        scn.FreepsPlayCard(aragorn);
        assertTrue(scn.FreepsCardPlayAvailable(remnant));
    }

    @Test
    public void RemnantTriggersOnShadowDiscardFromHand() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl remnant = scn.GetFreepsCard("remnant");

        scn.FreepsMoveCardToDiscard(boromir);
        scn.FreepsMoveCardToHand(aragorn);
        scn.FreepsMoveCardToSupportArea(remnant);

        PhysicalCardImpl inquisitor = scn.GetShadowCard("inquisitor");
        scn.ShadowMoveCardToHand(inquisitor);

        scn.StartGame();

        scn.SetTwilight(10);

        scn.FreepsSkipCurrentPhaseAction();

        assertEquals(1, scn.GetFreepsHandCount());
        assertEquals(1, scn.GetFreepsDeckCount()); //the orc is also in the deck
        assertEquals(1, scn.GetFreepsDiscardCount());

        scn.ShadowPlayCard(inquisitor);
        scn.ShadowAcceptOptionalTrigger();

        assertTrue(scn.FreepsHasOptionalTriggerAvailable());
        scn.FreepsAcceptOptionalTrigger();

       //scn.FreepsGetADParamAsList()

        scn.FreepsChoose("temp0");

        //Card from hand was discarded, one of the two cards in the discard was shuffled in with the orc
        assertEquals(0, scn.GetFreepsHandCount());
        assertEquals(2, scn.GetFreepsDeckCount());
        assertEquals(1, scn.GetFreepsDiscardCount());

        // 10 starting + 2 for moving - 5 for the orc + 1 for Remnant triggering
        assertEquals(8, scn.GetTwilight());

    }


}
