package com.gempukku.lotro.cards.official.set10;

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
public class Card_10_030_Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("endgame", "10_30");
                    put("aragorn", "1_89");

                    put("troop", "1_143");
                }}
        );
    }

    @Test
    public void EndoftheGameStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 10
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 0
         * Type: Event • Skirmish
         * Game Text: Make an exhausted [GONDOR] companion strength +2. If that companion wins this skirmish, heal that
         * companion or make him or her damage +1.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl endgame = scn.GetFreepsCard("endgame");

        assertEquals(0, endgame.getBlueprint().getTwilightCost());

        assertEquals(CardType.EVENT, endgame.getBlueprint().getCardType());
        assertEquals(Culture.GONDOR, endgame.getBlueprint().getCulture());
        assertTrue(scn.HasKeyword(endgame, Keyword.SKIRMISH)); // test for keywords as needed;
    }

    @Test
    public void DoesNotWorkIfNoExhaustedGondorCompanions() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl endgame = scn.GetFreepsCard("endgame");
        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(endgame);

        PhysicalCardImpl troop = scn.GetShadowCard("troop");
        scn.ShadowMoveCharToTable(troop);

        scn.StartGame();

        scn.AddWoundsToChar(frodo, 3);

        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.PassCurrentPhaseActions();
        scn.FreepsAssignToMinions(aragorn, troop);
        scn.FreepsResolveSkirmish(aragorn);

        //As Aragorn isn't exhausted (tho Frodo is), the event does nothing.
        assertTrue(scn.FreepsCardPlayAvailable(endgame));
        assertEquals(8, scn.GetStrength(aragorn));
        scn.FreepsPlayCard(endgame);
        assertEquals(8, scn.GetStrength(aragorn));
        //Passing skirmish actions
        scn.ShadowPassCurrentPhaseAction();
        scn.FreepsPassCurrentPhaseAction();
        //no other decisions occur from the event
        assertEquals(Phase.REGROUP, scn.GetCurrentPhase());
    }

    @Test
    public void AddsStrengthToExhaustedGondorCompanion() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl endgame = scn.GetFreepsCard("endgame");
        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(endgame);

        PhysicalCardImpl troop = scn.GetShadowCard("troop");
        scn.ShadowMoveCharToTable(troop);

        scn.StartGame();

        scn.AddWoundsToChar(aragorn, 3);

        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.PassCurrentPhaseActions();
        scn.FreepsAssignToMinions(aragorn, troop);
        scn.FreepsResolveSkirmish(aragorn);

        assertTrue(scn.FreepsCardPlayAvailable(endgame));

        assertEquals(8, scn.GetStrength(aragorn));
        scn.FreepsPlayCard(endgame);
        assertEquals(10, scn.GetStrength(aragorn));
    }

    @Test
    public void WinningSkirmishCanHealCompanion() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl endgame = scn.GetFreepsCard("endgame");
        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(endgame);

        PhysicalCardImpl troop = scn.GetShadowCard("troop");
        scn.ShadowMoveCharToTable(troop);

        scn.StartGame();

        scn.AddWoundsToChar(aragorn, 3);

        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.PassCurrentPhaseActions();
        scn.FreepsAssignToMinions(aragorn, troop);
        scn.FreepsResolveSkirmish(aragorn);

        assertTrue(scn.FreepsCardPlayAvailable(endgame));

        assertEquals(8, scn.GetStrength(aragorn));
        scn.FreepsPlayCard(endgame);
        assertEquals(10, scn.GetStrength(aragorn));
        scn.ShadowPassCurrentPhaseAction();
        scn.FreepsPassCurrentPhaseAction();

        assertTrue(scn.FreepsActionAvailable("Required trigger from"));
        scn.FreepsResolveActionOrder("Required trigger from");
        scn.FreepsChooseMultipleChoiceOption("Heal");
        assertEquals(2, scn.GetWoundsOn(aragorn));
        assertEquals(1, scn.GetWoundsOn(troop));
    }

    @Test
    public void WinningSkirmishCanAddDamageBonus() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl endgame = scn.GetFreepsCard("endgame");
        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(endgame);

        PhysicalCardImpl troop = scn.GetShadowCard("troop");
        scn.ShadowMoveCharToTable(troop);

        scn.StartGame();

        scn.AddWoundsToChar(aragorn, 3);

        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.PassCurrentPhaseActions();
        scn.FreepsAssignToMinions(aragorn, troop);
        scn.FreepsResolveSkirmish(aragorn);

        assertTrue(scn.FreepsCardPlayAvailable(endgame));

        assertEquals(8, scn.GetStrength(aragorn));
        scn.FreepsPlayCard(endgame);
        assertEquals(10, scn.GetStrength(aragorn));
        scn.ShadowPassCurrentPhaseAction();
        scn.FreepsPassCurrentPhaseAction();

        assertTrue(scn.FreepsActionAvailable("Required trigger from"));
        scn.FreepsResolveActionOrder("Required trigger from");
        scn.FreepsChooseMultipleChoiceOption("damage +1");
        assertEquals(3, scn.GetWoundsOn(aragorn));
        assertEquals(2, scn.GetWoundsOn(troop));
    }





}