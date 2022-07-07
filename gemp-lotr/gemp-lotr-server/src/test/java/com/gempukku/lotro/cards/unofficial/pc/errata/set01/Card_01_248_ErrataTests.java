package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_01_248_ErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("forces1", "51_248");
                    put("forces2", "51_248");
                    put("troll", "6_103");
                    put("orc1", "6_102");
                    put("orc2", "6_102");
                    put("orc3", "6_102");
                    put("orc4", "6_102");
                }}
        );
    }

    @Test
    public void ForcesStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 1E
         * Title: Forces of Mordor
         * Side: Shadow
         * Culture: Sauron
         * Twilight Cost: 0
         * Type: Event
         * Phase: Shadow
         * Errata Game Text: Shadow: Exert a [SAURON] minion to add (1) for each [SAURON] Orc you can spot (limit (3)).
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl forces = scn.GetFreepsCard("forces1");

        assertEquals(0, forces.getBlueprint().getTwilightCost());
        assertEquals(CardType.EVENT, forces.getBlueprint().getCardType());
        assertEquals(Culture.SAURON, forces.getBlueprint().getCulture());
    }

    @Test
    public void ExertsMinionWhenPlayed() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl forces1 = scn.GetShadowCard("forces1");
        PhysicalCardImpl forces2 = scn.GetShadowCard("forces2");
        PhysicalCardImpl troll = scn.GetShadowCard("troll");
        PhysicalCardImpl orc1 = scn.GetShadowCard("orc1");
        PhysicalCardImpl orc2 = scn.GetShadowCard("orc2");
        PhysicalCardImpl orc3 = scn.GetShadowCard("orc3");
        PhysicalCardImpl orc4 = scn.GetShadowCard("orc4");

        scn.ShadowMoveCardToHand(forces1,forces2);
        scn.ShadowMoveCharToTable(troll, orc1, orc2);

        scn.StartGame();

        scn.FreepsSkipCurrentPhaseAction();
        assertTrue(scn.ShadowActionAvailable("Forces of Mordor"));
        assertEquals(2, scn.GetTwilight());

        scn.ShadowPlayCard(forces1);

        assertTrue(scn.ShadowDecisionAvailable("Exert"));
        assertEquals(3, scn.GetShadowCardChoiceCount());
        scn.ShadowChoose(scn.ShadowGetCardChoices().get(1));

        //There are 3 minions, but only 2 orcs
        assertEquals(4, scn.GetTwilight());

        scn.ShadowMoveCharToTable(orc3, orc4);
        scn.ShadowPlayCard(forces2);
        scn.ShadowChoose(scn.ShadowGetCardChoices().get(1));

        //There are 5 minions, but only 4 orcs which should hit the limit of 3.
        assertEquals(7, scn.GetTwilight());

    }

}
