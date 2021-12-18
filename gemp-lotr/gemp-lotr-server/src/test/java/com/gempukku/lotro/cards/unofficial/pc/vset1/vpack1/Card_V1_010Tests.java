
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

public class Card_V1_010Tests
{
	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("darts", "151_10");
					put("galadriel", "1_45");
					put("greenleaf", "1_50");

					put("scout", "1_191");
					put("runner", "1_178");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void LetFlytheDartsStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: *Let Fly the Darts of Lindon
		* Side: Free Peoples
		* Culture: elven
		* Twilight Cost: 1
		* Type: condition
		* Subtype: 
		* Game Text: Tale.
		* 	 Bearer must be an [elven] companion.
		* 	Archery: If bearer is an archer, exert bearer to spot a minion. That minion cannot take archery wounds
		 * 	until the regroup phase. The Shadow player may remove (2) to prevent this.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darts = scn.GetFreepsCard("darts");

		assertTrue(darts.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(darts, Keyword.TALE)); // test for keywords as needed
		assertEquals(1, darts.getBlueprint().getTwilightCost());
		assertEquals(CardType.CONDITION, darts.getBlueprint().getCardType());
		assertEquals(Culture.ELVEN, darts.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, darts.getBlueprint().getSide());
	}

	@Test
	public void LetFlytheDartsOnlyPlaysOnElvenCompanions() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darts = scn.GetFreepsCard("darts");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl greenleaf = scn.GetFreepsCard("greenleaf");
		scn.FreepsMoveCardToHand(darts, galadriel, greenleaf);

		scn.StartGame();
		assertFalse(scn.FreepsActionAvailable("let fly the darts"));
		scn.FreepsPlayCard(galadriel);
		assertFalse(scn.FreepsActionAvailable("let fly the darts"));
		scn.FreepsPlayCard(greenleaf);
		assertTrue(scn.FreepsActionAvailable("let fly the darts"));
	}

	@Test
	public void LetFlytheDartsExertsToProtectMinionFromArrows() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darts = scn.GetFreepsCard("darts");
		PhysicalCardImpl greenleaf = scn.GetFreepsCard("greenleaf");
		scn.FreepsMoveCardToHand(darts, greenleaf);

		PhysicalCardImpl scout = scn.GetShadowCard("scout");
		PhysicalCardImpl runner = scn.GetShadowCard("runner");
		scn.ShadowMoveCharToTable(scout, runner);

		scn.StartGame();
		scn.FreepsPlayCard(greenleaf);
		scn.FreepsPlayCard(darts);
		scn.SetTwilight(0);

		scn.SkipToPhase(Phase.ARCHERY);
		assertEquals(4, scn.GetTwilight());
		assertEquals(0, scn.GetWoundsOn(greenleaf));
		assertTrue(scn.FreepsActionAvailable("let fly the darts"));
		scn.FreepsUseCardAction(darts);
		assertTrue(scn.FreepsDecisionAvailable("Choose"));
		scn.FreepsChooseCard(scout);
		assertTrue(scn.ShadowDecisionAvailable("Would you like to remove (2) to prevent a minion taking no archery wounds?"));
		scn.ShadowChooseNo();

		assertEquals(4, scn.GetTwilight());
		assertEquals(1, scn.GetWoundsOn(greenleaf));

		scn.ShadowSkipCurrentPhaseAction();
		scn.FreepsSkipCurrentPhaseAction();

		//Since the Scout was barred from taking archery wounds, Gemp automatically assigns the single arrow
		// to the Runner, killing it.
		assertEquals(Zone.DISCARD, runner.getZone());


	}

	@Test
	public void LetFlytheDartsPermitsShadowToPrevent() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darts = scn.GetFreepsCard("darts");
		PhysicalCardImpl greenleaf = scn.GetFreepsCard("greenleaf");
		scn.FreepsMoveCardToHand(darts, greenleaf);

		PhysicalCardImpl scout = scn.GetShadowCard("scout");
		PhysicalCardImpl runner = scn.GetShadowCard("runner");
		scn.ShadowMoveCharToTable(scout, runner);

		scn.StartGame();
		scn.FreepsPlayCard(greenleaf);
		scn.FreepsPlayCard(darts);
		scn.SetTwilight(0);

		scn.SkipToPhase(Phase.ARCHERY);
		assertEquals(4, scn.GetTwilight());
		assertEquals(0, scn.GetWoundsOn(greenleaf));
		assertTrue(scn.FreepsActionAvailable("let fly the darts"));
		scn.FreepsUseCardAction(darts);
		assertTrue(scn.FreepsDecisionAvailable("Choose"));
		scn.FreepsChooseCard(scout);
		assertTrue(scn.ShadowDecisionAvailable("Would you like to remove (2) to prevent a minion taking no archery wounds?"));
		scn.ShadowChooseYes();

		assertEquals(2, scn.GetTwilight());
		assertEquals(1, scn.GetWoundsOn(greenleaf));

		scn.ShadowSkipCurrentPhaseAction();
		scn.FreepsSkipCurrentPhaseAction();

		//There should be 2 options, 1 for each minion that arrows can be assigned to.
		assertEquals(2, scn.ShadowGetADParam("cardId").length);
	}
}
