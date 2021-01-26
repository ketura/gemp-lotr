package com.gempukku.lotro.cards.unofficial.pc.errata;

import com.gempukku.lotro.cards.GenericCardTest;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;

public class Elrond_LoRErrataTest
{

    protected GenericCardTest GetSimpleDeckScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("elrond", "21_1040");
                    put("randomcard", "1_3");
                }},
                null,
                null,
                null
        );
    }

    protected GenericCardTest GetSimpleSpotScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("elrond", "21_1040");
                    put("gandalf", "1_72");
                    put("arwen", "1_30");
                }},
                null,
                null,
                null
        );
    }

    protected GenericCardTest GetHome3AllyScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("elrond", "21_1040");
                    put("ally1", "1_60");
                    put("ally2", "1_27");
                }},
                null,
                null,
                null
        );
    }

    @Test
    public void FellowshipActionExertsTwiceToDrawACard() throws DecisionResultInvalidException, CardNotFoundException
    {
        //Pre-game setup
        GenericCardTest scenario = GetSimpleDeckScenario();
        PhysicalCardImpl elrond = scenario.freepsCards.get("elrond");
        scenario.FreepsMoveCharToTable(elrond);

        scenario.StartGame();

        scenario.FreepsMoveCardToDeck("randomcard");

        Assert.assertEquals(Phase.FELLOWSHIP, scenario.GetCurrentPhase());
        Assert.assertTrue(scenario.FreepsActionAvailable("Use Elrond"));

        Assert.assertEquals(0, scenario.GetWoundsOn(elrond));
        Assert.assertEquals(0, scenario.GetFreepsHandCount());
        Assert.assertEquals(1, scenario.GetFreepsDeckCount());

        scenario.FreepsUseAction("Use Elrond");

        Assert.assertEquals(2, scenario.GetWoundsOn(elrond));
        Assert.assertEquals(0, scenario.GetFreepsDeckCount());
        Assert.assertEquals(1, scenario.GetFreepsHandCount());

    }

    @Test
    public void CardCanPlayIfGandalfInPlay() throws DecisionResultInvalidException, CardNotFoundException
    {
        //Pre-game setup
        GenericCardTest scenario = GetSimpleSpotScenario();
        scenario.FreepsMoveCardToHand("elrond");
        scenario.FreepsMoveCardToHand("gandalf");

        scenario.StartGame();

        Assert.assertEquals(Phase.FELLOWSHIP, scenario.GetCurrentPhase());
        Assert.assertFalse(scenario.FreepsActionAvailable("Play Elrond"));

        scenario.FreepsPlayCharFromHand("gandalf");
        Assert.assertTrue(scenario.FreepsActionAvailable("Play Elrond"));
    }

    @Test
    public void CardCanPlayIfElfInPlay() throws DecisionResultInvalidException, CardNotFoundException
    {
        //Pre-game setup
        GenericCardTest scenario = GetSimpleSpotScenario();
        scenario.FreepsMoveCardToHand("elrond");
        scenario.FreepsMoveCardToHand("arwen");

        scenario.StartGame();

        Assert.assertEquals(Phase.FELLOWSHIP, scenario.GetCurrentPhase());
        Assert.assertFalse(scenario.FreepsActionAvailable("Play Elrond"));

        scenario.FreepsPlayCharFromHand("arwen");
        Assert.assertTrue(scenario.FreepsActionAvailable("Play Elrond"));
    }

    @Test
    public void AllyHealsCappedAt2() throws DecisionResultInvalidException, CardNotFoundException
    {
        //Pre-game setup
        GenericCardTest scenario = GetHome3AllyScenario();
        scenario.FreepsMoveCharToTable("elrond");
        scenario.FreepsMoveCharToTable("ally1");
        scenario.FreepsMoveCharToTable("ally2");

        scenario.FreepsAddWoundsToChar("elrond", 1);
        scenario.FreepsAddWoundsToChar("ally1", 1);
        scenario.FreepsAddWoundsToChar("ally2", 1);

        scenario.StartGame();

        Assert.assertEquals(Phase.BETWEEN_TURNS, scenario.GetCurrentPhase());

        Assert.assertEquals(3, scenario.FreepsGetADParamAsList("cardId").size());
        Assert.assertEquals("0", scenario.FreepsGetADParam("min"));
        Assert.assertEquals("2", scenario.FreepsGetADParam("max"));
    }
}
