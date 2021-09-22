package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Card_V1_002Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("gimli", "1_13");
                    put("axe", "1_9");
                    put("axe2", "1_9");
                    put("deep", "151_2");
                    put("beneath", "2_1");

                    put("runner", "1_178");
                    put("plunder", "1_193");

                }}
        );
    }


    @Test
    public void WhiteCityStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: *Deepest Delvings
         * Side: Free Peoples
         * Culture: Dwarven
         * Twilight Cost: 1
         * Type: Condition
         * Subtype: Support Area
         * Game Text: Each time you discard a [dwarven] card from the top of your deck you may add (1) to stack that card here.
         *  Maneuver: Exert a dwarf to take a card stacked here into hand.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl whitecity = scn.GetFreepsCard("whitecity");

        assertTrue(scn.HasKeyword(whitecity, Keyword.SUPPORT_AREA));
        assertEquals(2, whitecity.getBlueprint().getTwilightCost());
        assertTrue(whitecity.getBlueprint().isUnique());
    }

    @Test
    public void topdecktest() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl beneath = scn.GetFreepsCard("beneath");
        PhysicalCardImpl deep = scn.GetFreepsCard("deep");
        scn.FreepsMoveCardToSupportArea(beneath);
        scn.FreepsMoveCardToSupportArea(deep);
        scn.FreepsMoveCharToTable("gimli");
        scn.FreepsMoveCardToDiscard("axe");

        scn.StartGame();

        scn.FreepsUseCardAction(beneath);


        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.SkipCurrentPhaseActions();


    }


}
