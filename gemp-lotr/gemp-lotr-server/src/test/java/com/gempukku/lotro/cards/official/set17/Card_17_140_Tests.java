package com.gempukku.lotro.cards.official.set17;

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

// Wielder of the Flame
public class Card_17_140_Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("appetite", "1_294");
                    put("enquea", "17_140");
                    put("dt", "12_163");
                    put("rider", "12_161");

                    put("moria1", "1_21");
                    put("moria2", "1_21");
                    put("moria3", "1_21");
                    put("moria4", "1_21");
                    put("moria5", "1_21");
                    put("moria6", "1_21");

                    put("endgame", "10_30");
                    put("aragorn", "1_89");
                    put("troop", "1_143");
                }}
        );
    }

    @Test
    public void EnqueaStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 17
         * Title: Ulaire Enquea
         * Subtitle: Duplicitous Lieutenant
         * Side: Shadow
         * Culture: Wraith
         * Twilight Cost: 6
         * Type: Minion
         * Race: Nazgul
         * Strength: 11
         * Vitality: 4
         * Site: 3
         * Game Text: Fierce.
         * Each time the Free Peoples player heals a companion, you may add a burden.
         * Maneuver: Exert Ulaire Enquea twice to discard a condition (or 2 conditions if you can spot 5 Free Peoples player's conditions).
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl enquea = scn.GetFreepsCard("enquea");

        assertTrue(enquea.getBlueprint().isUnique());
        assertEquals(6, enquea.getBlueprint().getTwilightCost());
        assertTrue(scn.HasKeyword(enquea, Keyword.FIERCE)); // test for keywords as needed
        assertEquals(CardType.MINION, enquea.getBlueprint().getCardType());
        assertEquals(Culture.WRAITH, enquea.getBlueprint().getCulture());
        assertEquals(Race.NAZGUL, enquea.getBlueprint().getRace());
        assertEquals(11, enquea.getBlueprint().getStrength());
        assertEquals(4, enquea.getBlueprint().getVitality());
        assertEquals(3, enquea.getBlueprint().getSiteNumber());
    }

    @Test
    public void HealingFromShadowSourcesDoesNothing() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl appetite = scn.GetFreepsCard("appetite");
        scn.FreepsMoveCardToHand(appetite);

        PhysicalCardImpl enquea = scn.GetShadowCard("enquea");
        PhysicalCardImpl rider = scn.GetShadowCard("rider");
        PhysicalCardImpl dt = scn.GetShadowCard("dt");
        scn.ShadowMoveCharToTable(enquea);
        scn.ShadowMoveCardToHand(rider);
        scn.ShadowMoveCardToHand(dt);

        scn.StartGame();

        scn.RemoveBurdens(1); //To compensate for the bid
        scn.AddWoundsToChar(frodo, 3);

        assertEquals(0, scn.GetBurdens());
        scn.FreepsPlayCard(appetite);
        scn.FreepsChoose("2");
        scn.ShadowAcceptOptionalTrigger();
        scn.ShadowAcceptOptionalTrigger();

        //Two burdens added from enquea's game text from freeps healing
        assertEquals(2, scn.GetBurdens());

        scn.AddWoundsToChar(frodo, 2);
        scn.SetTwilight(10);

        //moving
        scn.FreepsSkipCurrentPhaseAction();

        scn.ShadowPlayCard(dt);
        assertEquals(2, scn.GetBurdens());
        scn.ShadowPlayCard(rider);
        scn.ShadowAcceptOptionalTrigger();
        //We should have the DT trigger, but no Enquea triggers
        assertFalse(scn.ShadowHasOptionalTriggerAvailable());
        //We should see +1 burden from Dark Temptation, but no burdens added from Enquea as DT is a shadow card.
        assertEquals(3, scn.GetBurdens());
        assertEquals(1, scn.GetWoundsOn(frodo));
    }

    @Test
    public void ManeuverActionDiscardsOneOrTwoConditions() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl appetite = scn.GetFreepsCard("appetite");

        PhysicalCardImpl moria1 = scn.GetFreepsCard("moria1");
        PhysicalCardImpl moria2 = scn.GetFreepsCard("moria2");
        PhysicalCardImpl moria3 = scn.GetFreepsCard("moria3");
        PhysicalCardImpl moria4 = scn.GetFreepsCard("moria4");
        PhysicalCardImpl moria5 = scn.GetFreepsCard("moria5");
        PhysicalCardImpl moria6 = scn.GetFreepsCard("moria6");

        scn.FreepsMoveCardToSupportArea(moria1);
        scn.FreepsMoveCardToSupportArea(moria2);
        scn.FreepsMoveCardToSupportArea(moria3);
        scn.FreepsMoveCardToSupportArea(moria4);

        PhysicalCardImpl enquea = scn.GetShadowCard("enquea");
        scn.ShadowMoveCharToTable(enquea);

        scn.StartGame();

        scn.SkipToPhase(Phase.MANEUVER);
        scn.FreepsSkipCurrentPhaseAction();

        assertTrue(scn.ShadowActionAvailable("Use Úlairë Enquëa, Duplicitous Lieutenant"));

        scn.ShadowUseCardAction(enquea);
        assertTrue(scn.ShadowDecisionAvailable("Choose cards to discard"));
        scn.ShadowChooseCard(moria4);

        //with only 4 conditions in play, Enquea should only be requesting a single condition to be discarded.
        assertFalse(scn.ShadowAnyDecisionsAvailable());

        scn.RemoveWoundsFromChar(enquea, 2);
        scn.FreepsMoveCardToSupportArea(moria5);
        scn.FreepsMoveCardToSupportArea(moria6);
        scn.FreepsSkipCurrentPhaseAction();
        //There should now be 4 - 1 + 2 = 5 freeps conditions in play.

        scn.ShadowUseCardAction(enquea);
        assertTrue(scn.ShadowDecisionAvailable("Choose cards to discard"));
        scn.ShadowChooseCards(moria5, moria6);
    }

    @Test
    public void EndOfTheGameTriggersEnquea() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl endgame = scn.GetFreepsCard("endgame");
        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(endgame);

        PhysicalCardImpl troop = scn.GetShadowCard("troop");
        scn.ShadowMoveCharToTable(troop);

        PhysicalCardImpl enquea = scn.GetShadowCard("enquea");
        scn.ShadowMoveCharToTable(enquea);

        scn.StartGame();

        scn.RemoveBurdens(1); //To compensate for the bid
        scn.AddWoundsToChar(aragorn, 3);

        //moving
        scn.SkipToPhase(Phase.ASSIGNMENT);

        scn.SkipCurrentPhaseActions();
        scn.FreepsAssignToMinions(aragorn, troop);
        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsResolveSkirmish(aragorn);

        scn.FreepsPlayCard(endgame);

        scn.ShadowSkipCurrentPhaseAction();
        scn.FreepsSkipCurrentPhaseAction();

        scn.FreepsResolveActionOrder("Required trigger from");
        scn.FreepsChooseMultipleChoiceOption("Heal");
        assertEquals(2, scn.GetWoundsOn(aragorn));

        //As a Freeps healing source, we should have the Enquea trigger
        assertTrue(scn.ShadowHasOptionalTriggerAvailable());

    }



}