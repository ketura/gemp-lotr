package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;


public class Card_01_019_Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<>() {{
                    put("balin", "1_19");
                    put("gimli", "1_13");

                    put("runner1", "1_178");
                    put("runner2", "1_178");
                    put("scout", "1_191");

                }}
        );
    }

    @Test
    public void HereLiesBalinStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 1
         * Title: Here Lies Balin, Son of Fundin
         * Side: Free Peoples
         * Culture: Dwarven
         * Twilight Cost: 0
         * Type: Event
         * Subtype: Maneuver
         * Game Text: Maneuver: Exert a Dwarf to wound 2 Orcs or to wound 1 Orc twice.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl balin = scn.GetFreepsCard("balin");

        assertFalse(balin.getBlueprint().isUnique());
        assertEquals(Side.FREE_PEOPLE, balin.getBlueprint().getSide());
        assertEquals(Culture.DWARVEN, balin.getBlueprint().getCulture());
        assertEquals(CardType.EVENT, balin.getBlueprint().getCardType());
        //assertEquals(Race.CREATURE, balin.getBlueprint().getRace());
        assertTrue(scn.HasKeyword(balin, Keyword.MANEUVER)); // test for keywords as needed
        assertEquals(0, balin.getBlueprint().getTwilightCost());
        //assertEquals(, balin.getBlueprint().getStrength());
        //assertEquals(, balin.getBlueprint().getVitality());
        //assertEquals(, balin.getBlueprint().getResistance());
        //assertEquals(Signet., balin.getBlueprint().getSignet());
        //assertEquals(, balin.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies

    }

    @Test
    public void BalinCanWound2OrcsOnce() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl balin = scn.GetFreepsCard("balin");
        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(balin);

        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");
        PhysicalCardImpl scout = scn.GetShadowCard("scout");
        scn.ShadowMoveCharToTable(runner1, runner2);

        scn.StartGame();

        scn.SkipToPhase(Phase.MANEUVER);

        assertEquals(0, scn.GetWoundsOn(gimli));
        assertEquals(Zone.SHADOW_CHARACTERS, runner1.getZone());
        assertEquals(Zone.SHADOW_CHARACTERS, runner2.getZone());

        assertTrue(scn.FreepsCardPlayAvailable(balin));
        scn.FreepsPlayCard(balin);

        assertTrue(scn.FreepsDecisionAvailable("Choose action"));
        scn.FreepsChooseMultipleChoiceOption("Wound 2 Orcs");

        assertEquals(1, scn.GetWoundsOn(gimli));
        assertEquals(Zone.DISCARD, runner1.getZone());
        assertEquals(Zone.DISCARD, runner2.getZone());
    }

    @Test
    public void BalinCanWound1OrcTwice() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl balin = scn.GetFreepsCard("balin");
        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(balin);

        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");
        PhysicalCardImpl scout = scn.GetShadowCard("scout");
        scn.ShadowMoveCharToTable(scout);

        scn.StartGame();

        scn.SkipToPhase(Phase.MANEUVER);

        assertEquals(0, scn.GetWoundsOn(gimli));
        assertEquals(Zone.SHADOW_CHARACTERS, scout.getZone());
        assertEquals(0, scn.GetWoundsOn(scout));

        assertTrue(scn.FreepsCardPlayAvailable(balin));
        scn.FreepsPlayCard(balin);

        assertEquals(1, scn.GetWoundsOn(gimli));
        assertEquals(Zone.DISCARD, scout.getZone());
    }

    @Test
    public void FreepsMustChooseTwoOrcOrOneOrcToWoundTwice() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl balin = scn.GetFreepsCard("balin");
        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(balin);

        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");
        PhysicalCardImpl scout = scn.GetShadowCard("scout");
        scn.ShadowMoveCharToTable(runner1, runner2, scout);

        scn.StartGame();

        scn.SkipToPhase(Phase.MANEUVER);

        assertEquals(0, scn.GetWoundsOn(gimli));
        assertEquals(Zone.SHADOW_CHARACTERS, scout.getZone());
        assertEquals(0, scn.GetWoundsOn(scout));

        assertTrue(scn.FreepsCardPlayAvailable(balin));
        scn.FreepsPlayCard(balin);

        assertTrue(scn.FreepsDecisionAvailable("Choose action"));
        assertEquals(2, scn.FreepsGetMultipleChoices().size());
    }

    @Test
    public void OneOrcWith1VitalityCanBeHitByHereLiesBalin() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl balin = scn.GetFreepsCard("balin");
        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(balin);

        PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
        PhysicalCardImpl runner2 = scn.GetShadowCard("runner2");
        PhysicalCardImpl scout = scn.GetShadowCard("scout");
        scn.ShadowMoveCharToTable(runner1);

        scn.StartGame();

        scn.SkipToPhase(Phase.MANEUVER);

        assertEquals(0, scn.GetWoundsOn(gimli));
        assertEquals(Zone.SHADOW_CHARACTERS, runner1.getZone());
        assertEquals(0, scn.GetWoundsOn(runner1));

        assertTrue(scn.FreepsCardPlayAvailable(balin));
        scn.FreepsPlayCard(balin);

        //https://wiki.lotrtcgpc.net/wiki/Comprehensive_Rules_4.1#effect
        //"If the effect of a card or special ability requires you to choose one of two different actions,
        // you must choose an action that you are fully capable of performing (if possible)."
        // As a result, if you can neither "wound 2 orcs" or "wound 1 orc twice", none of the choices are possible.
        // You then back up and choose the option you are most capable of performing (I guess)

        assertEquals(Zone.DISCARD, runner1.getZone());
    }





}