package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_008_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("lament", "151_8");
					put("gandalf", "1_364");
					put("elrond", "7_21");
					put("galadriel", "10_11");
					put("orophin", "1_56");
					put("gimli", "1_13");
					put("farin", "1_11");
					put("guard", "1_7");

					put("axe1", "1_9");
					put("axe2", "1_9");
					put("axe3", "1_9");
					put("runner1", "1_178");
					put("runner2", "1_178");

					// put other cards in here as needed for the test case
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void LamentForGandalfStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		 * Set: V1
		 * Title: *Lament For Gandalf
		 * Side: Free Peoples
		 * Culture: gandalf
		 * Twilight Cost: 1
		 * Type: condition
		 * Subtype: Support Area
		 * Game Text: At the start of the Regroup phase you may spot Gandalf to stack a card from hand here.
		 * 	Response: If Gandalf is killed you may exert X companions, or [elven] allies to take X cards stacked here into hand.
		 */

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl card = scn.GetFreepsCard("lament");

		assertEquals(Side.FREE_PEOPLE, card.getBlueprint().getSide());
		assertEquals(Culture.ELVEN, card.getBlueprint().getCulture());
		assertEquals(CardType.CONDITION, card.getBlueprint().getCardType());
		assertTrue(card.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(card, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(1, card.getBlueprint().getTwilightCost());
		//assertEquals(, card.getBlueprint().getStrength());
		//assertEquals(, card.getBlueprint().getVitality());
		//assertEquals(, card.getBlueprint().getResistance());
		//assertEquals(Signet., card.getBlueprint().getSignet());
		//assertEquals(, card.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies
	}

	@Test
	public void LamentSpotsGandalfToStackAtTheStartOfManeuver() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl lament = scn.GetFreepsCard("lament");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl axe1 = scn.GetFreepsCard("axe1");
		PhysicalCardImpl axe2 = scn.GetFreepsCard("axe2");
		PhysicalCardImpl runner2 = scn.GetFreepsCard("runner2");

		scn.FreepsMoveCardToHand(lament, axe1, axe2, runner2);

		PhysicalCardImpl runner1 = scn.GetShadowCard("runner1");
		scn.ShadowMoveCharToTable(runner1);

		scn.StartGame();
		scn.FreepsPlayCard(lament);

		scn.SkipToPhase(Phase.MANEUVER);

		assertFalse(scn.FreepsHasOptionalTriggerAvailable());

		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.FreepsChooseToMove();

		scn.FreepsMoveCharToTable(gandalf);

		scn.SkipToPhase(Phase.MANEUVER);

		assertTrue(scn.FreepsHasOptionalTriggerAvailable());
		scn.FreepsAcceptOptionalTrigger();
		assertTrue(scn.FreepsDecisionAvailable("Choose cards to stack"));
		assertEquals(3, scn.GetFreepsHandCount());
		assertEquals(2, scn.GetFreepsCardChoiceCount()); // 3 cards in hand, but only 2 freeps cards

		scn.FreepsChooseCard(axe1);
		assertEquals(Zone.STACKED, axe1.getZone());
		assertEquals(lament, axe1.getStackedOn());
		assertEquals(1, scn.GetStackedCards(lament).size());
	}

	@Test
	public void RegroupActionExertsAndTakesCardsBasedOnDeadPileCount() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl lament = scn.GetFreepsCard("lament");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		PhysicalCardImpl axe1 = scn.GetFreepsCard("axe1");
		PhysicalCardImpl axe2 = scn.GetFreepsCard("axe2");

		scn.FreepsMoveCharToTable(gandalf, galadriel, elrond, orophin);
		scn.FreepsMoveCardToSupportArea(lament);
		scn.StackCardsOn(lament, axe1, axe2);

		//Max out the move limit so we don't have to juggle play back and forth
		scn.ApplyAdHocModifier(new MoveLimitModifier(null, 10));

		scn.StartGame();

		scn.SkipToPhase(Phase.REGROUP);

		assertEquals(0, scn.GetFreepsDeadCount());
		assertFalse(scn.FreepsHasOptionalTriggerAvailable());

		scn.FreepsMoveCardToDeadPile("guard");

		scn.PassCurrentPhaseActions();
		scn.FreepsChooseToMove();
		scn.SkipToPhase(Phase.REGROUP);

		// 1 dead comp, but it's non-unique so it shouldn't trigger
		assertEquals(1, scn.GetFreepsDeadCount());
		assertFalse(scn.FreepsHasOptionalTriggerAvailable());

		scn.FreepsMoveCardToDeadPile("gimli", "farin");

		scn.PassCurrentPhaseActions();
		scn.ShadowPassCurrentPhaseAction(); // reconciliation
		scn.FreepsChooseToMove();
		scn.SkipToPhase(Phase.REGROUP);

		// 3 comps, at least 1 of which is unique, should trigger
		assertEquals(3, scn.GetFreepsDeadCount());
		assertTrue(scn.FreepsHasOptionalTriggerAvailable());

		scn.FreepsAcceptOptionalTrigger();

		assertTrue( scn.FreepsDecisionAvailable("Choose how many dead unique companions to spot"));
		// 2 unique comps in the dead pile should restrict the max
		assertEquals("2", scn.FreepsGetADParam("max")[0]);

		scn.FreepsChoose("1");
		assertTrue(scn.FreepsDecisionAvailable("Choose cards to exert"));
		assertEquals(3, scn.GetFreepsCardChoiceCount());

		assertEquals(0, scn.GetWoundsOn(elrond));
		scn.FreepsChooseCard(elrond);

		assertTrue(scn.FreepsDecisionAvailable("Choose card"));
		assertEquals(2, scn.GetStackedCards(lament).size());
		assertEquals(2, scn.GetFreepsCardChoiceCount());

		assertEquals(1, scn.GetWoundsOn(elrond));
		assertEquals(0, scn.GetFreepsHandCount());
		scn.FreepsChooseCard(axe1);

		assertEquals(Zone.HAND, axe1.getZone());
		assertEquals(Zone.STACKED, axe2.getZone());
		assertEquals(1, scn.GetFreepsHandCount());

	}
}
