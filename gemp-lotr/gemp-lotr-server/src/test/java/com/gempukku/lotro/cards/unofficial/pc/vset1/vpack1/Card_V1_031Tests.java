
package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_031Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("purpose", "151_31");
					put("merry", "1_303");
					put("pippin", "1_306");
					put("aragorn", "1_89");

					put("ftentacle1", "2_58");
					put("ftentacle2", "2_58");
					put("ftentacle3", "2_58");
					put("ftentacle4", "2_58");
					put("htentacle", "2_66");

				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void PurposeStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: Guided by One Purpose
		* Side: Free Peoples
		* Culture: moria
		* Twilight Cost: 0
		* Type: condition
		* Subtype: Support Area
		* Game Text: Each time a tentacle wins a skirmish you may stack it here.
		* 	Assignment: Spot 4 tentacles here and discard this condition to assign a [moria] creature to the Ring-bearer. The Free Peoples player may exert the Ring-bearer twice to prevent this.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl purpose = scn.GetFreepsCard("purpose");

		assertFalse(purpose.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(purpose, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(0, purpose.getBlueprint().getTwilightCost());
		assertEquals(CardType.CONDITION, purpose.getBlueprint().getCardType());
		assertEquals(Culture.MORIA, purpose.getBlueprint().getCulture());
		assertEquals(Side.SHADOW, purpose.getBlueprint().getSide());
	}

	@Test
	public void PurposeStacksMinionsThatWin() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl merry = scn.GetFreepsCard("merry");
		PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
		scn.FreepsMoveCharToTable(merry, aragorn);

		PhysicalCardImpl purpose = scn.GetShadowCard("purpose");
		PhysicalCardImpl ftentacle1 = scn.GetShadowCard("ftentacle1");
		PhysicalCardImpl ftentacle2 = scn.GetShadowCard("ftentacle2");
		scn.ShadowMoveCardToHand(purpose, ftentacle1, ftentacle2);

		scn.StartGame();
		scn.SetTwilight(8);
		scn.ApplyAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.MARSH));
		scn.FreepsSkipCurrentPhaseAction();
		scn.ShadowPlayCard(purpose);
		scn.ShadowPlayCard(ftentacle1);
		scn.ShadowDeclineOptionalTrigger();
		scn.ShadowPlayCard(ftentacle2);
		scn.ShadowDeclineOptionalTrigger();

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.FreepsSkipCurrentPhaseAction();
		assertFalse(scn.ShadowActionAvailable("One Purpose"));
		scn.ShadowSkipCurrentPhaseAction();
		scn.FreepsAssignToMinions(new PhysicalCardImpl[]{merry, ftentacle1}, new PhysicalCardImpl[]{aragorn,ftentacle2});

		scn.FreepsResolveSkirmish(merry);
		scn.SkipCurrentPhaseActions();
		assertTrue(scn.ShadowHasOptionalTriggerAvailable());
		scn.ShadowAcceptOptionalTrigger();
		assertEquals(1, scn.GetStackedCards(purpose).size());

		scn.FreepsResolveSkirmish(aragorn);
		scn.SkipCurrentPhaseActions();
		assertFalse(scn.ShadowHasOptionalTriggerAvailable());
		assertEquals(1, scn.GetStackedCards(purpose).size());

	}

	@Test
	public void PurposeSpots4StackedTentaclesToAssignToRB() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl merry = scn.GetFreepsCard("merry");
		PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
		scn.FreepsMoveCharToTable(merry, aragorn);

		PhysicalCardImpl purpose = scn.GetShadowCard("purpose");
		PhysicalCardImpl htentacle = scn.GetShadowCard("htentacle");
		scn.ShadowMoveCardToHand(htentacle);
		scn.ShadowMoveCardToSupportArea(purpose);
		scn.StackCardsOn(purpose,scn.GetShadowCard("ftentacle1"), scn.GetShadowCard("ftentacle2"), scn.GetShadowCard("ftentacle3"), scn.GetShadowCard("ftentacle4"));

		scn.StartGame();
		scn.SetTwilight(4);
		scn.ApplyAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.MARSH));
		scn.FreepsSkipCurrentPhaseAction();
		scn.ShadowPlayCard(htentacle);
		scn.ShadowDeclineOptionalTrigger();

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.FreepsSkipCurrentPhaseAction();
		assertTrue(scn.ShadowActionAvailable("One Purpose"));
		scn.ShadowUseCardAction(purpose);
		assertTrue(scn.IsCharAssigned(scn.GetRingBearer()));
		//should now see One Purpose and the four tentacles stacked on it in the discard pile
		assertEquals(5, scn.GetShadowDiscardCount());
	}
}
