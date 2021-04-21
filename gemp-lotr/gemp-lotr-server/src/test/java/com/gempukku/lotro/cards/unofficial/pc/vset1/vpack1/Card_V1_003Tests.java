package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_003Tests
{

    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("boromir", "101_3");
                    put("sam", "1_311");
                    put("merry", "1_302");
                    put("pippin", "1_307");
                }}
        );
    }


    @Test
    public void BoromirStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: VSet1, VPack1
         * Title: *Boromir
         * Subtitle: The Redeemed
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 3
         * Type: Companion
         * Subtype: Man
         * Strength: 7
         * Vitality: 3
         * Signet: Aragorn
         * Game Text: While you can spot 2 [shire] companions, Boromir is defender +1.
         * While you can spot 4 [shire] companions, Boromir is strength +2 and damage +1.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");

        assertTrue(boromir.getBlueprint().isUnique());
        assertEquals(3, boromir.getBlueprint().getTwilightCost());
        assertEquals(7, boromir.getBlueprint().getStrength());
        assertEquals(3, boromir.getBlueprint().getVitality());
        assertEquals(Signet.ARAGORN, boromir.getBlueprint().getSignet());
    }

    @Test
    public void TwoAndFourHobbitsTriggerBuffs() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl sam = scn.GetFreepsCard("sam");
        PhysicalCardImpl merry = scn.GetFreepsCard("merry");
        PhysicalCardImpl pippin = scn.GetFreepsCard("pippin");

        scn.FreepsMoveCardToHand(sam, merry, pippin);
        scn.FreepsMoveCharToTable(boromir);

        scn.StartGame();

        assertFalse(scn.HasKeyword(boromir, Keyword.DEFENDER));
        assertFalse(scn.HasKeyword(boromir, Keyword.DAMAGE));
        assertEquals(7, scn.GetStrength(boromir));

        scn.FreepsPlayCard(sam);
        assertTrue(scn.HasKeyword(boromir, Keyword.DEFENDER));
        assertEquals(7, scn.GetStrength(boromir));

        scn.FreepsPlayCard(merry);
        scn.FreepsPlayCard(pippin);
        assertTrue(scn.HasKeyword(boromir, Keyword.DAMAGE));
        assertEquals(9, scn.GetStrength(boromir));
    }


}
