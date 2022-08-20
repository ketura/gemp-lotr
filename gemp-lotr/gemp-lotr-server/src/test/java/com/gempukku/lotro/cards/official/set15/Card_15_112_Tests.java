package com.gempukku.lotro.cards.official.set15;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_15_112_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("troll", "15_112");
					put("orc1", "13_118");
					put("orc2", "13_118");
					put("orc3", "13_118");
					put("orc4", "13_118");
					put("orc5", "13_118");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void MountaintrollStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 15
		* Title: Mountain-troll
		* Side: Free Peoples
		* Culture: Orc
		* Twilight Cost: 10
		* Type: minion
		* Subtype: Troll
		* Strength: 13
		* Vitality: 4
		* Site Number: 5
		* Game Text: When you play this minion, you may discard 5 [orc] minions from play to make it twilight cost -10 and
		 * <b>fierce</b>.
		 * Shadow: Remove (2) to play an [orc] Orc from your discard pile. It comes into play exhausted.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl troll = scn.GetFreepsCard("troll");

		assertFalse(troll.getBlueprint().isUnique());
		assertEquals(Side.SHADOW, troll.getBlueprint().getSide());
		assertEquals(Culture.ORC, troll.getBlueprint().getCulture());
		assertEquals(CardType.MINION, troll.getBlueprint().getCardType());
		assertEquals(Race.TROLL, troll.getBlueprint().getRace());
		assertEquals(10, troll.getBlueprint().getTwilightCost());
		assertEquals(22, troll.getBlueprint().getStrength());
		assertEquals(6, troll.getBlueprint().getVitality());
		//assertEquals(, troll.getBlueprint().getResistance());
		//assertEquals(Signet., troll.getBlueprint().getSignet());
		assertEquals(5, troll.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void OnPlayTrollDoesNothingIfLessThan5OrcMinionsInPlay() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		var troll = scn.GetShadowCard("troll");
		var orc1 = scn.GetShadowCard("orc1");
		var orc2 = scn.GetShadowCard("orc2");
		var orc3 = scn.GetShadowCard("orc3");
		var orc4 = scn.GetShadowCard("orc4");
		var orc5 = scn.GetShadowCard("orc5");
		scn.ShadowMoveCardToHand(troll);
		scn.ShadowMoveCharToTable(orc1, orc2, orc3, orc4);

		scn.StartGame();

		scn.SetTwilight(20);

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowPlayCard(troll);

		assertFalse(scn.ShadowHasOptionalTriggerAvailable());
	}

	@Test
	public void OnPlayTrollOptionallDiscards5OrcMinionsForMinus10TwilightAndFierceUntilEndOfTurn() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		var troll = scn.GetShadowCard("troll");
		var orc1 = scn.GetShadowCard("orc1");
		var orc2 = scn.GetShadowCard("orc2");
		var orc3 = scn.GetShadowCard("orc3");
		var orc4 = scn.GetShadowCard("orc4");
		var orc5 = scn.GetShadowCard("orc5");
		scn.ShadowMoveCardToHand(troll);
		scn.ShadowMoveCharToTable(orc1, orc2, orc3, orc4, orc5);

		scn.StartGame();

		scn.SetTwilight(17);

		scn.FreepsPassCurrentPhaseAction();
		assertEquals(20, scn.GetTwilight());
		scn.ShadowPlayCard(troll);

		assertTrue(scn.ShadowDecisionAvailable("choose cards to discard"));
		assertFalse(scn.HasKeyword(troll, Keyword.FIERCE));

		scn.ShadowChooseYes();
		assertTrue(scn.HasKeyword(troll, Keyword.FIERCE));
		assertEquals(Zone.DISCARD, orc1.getZone());
		assertEquals(Zone.DISCARD, orc2.getZone());
		assertEquals(Zone.DISCARD, orc3.getZone());
		assertEquals(Zone.DISCARD, orc4.getZone());
		assertEquals(Zone.DISCARD, orc5.getZone());
		assertEquals(18, scn.GetTwilight()); //20 initial -2 for roaming, troll was otherwise free

		scn.SkipToPhase(Phase.ASSIGNMENT);
		assertTrue(scn.HasKeyword(troll, Keyword.FIERCE));

		scn.SkipToPhase(Phase.REGROUP);
		assertTrue(scn.HasKeyword(troll, Keyword.FIERCE));

		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();
		assertTrue(scn.HasKeyword(troll, Keyword.FIERCE));
	}

	@Test
	public void ShadowAbilityRemoves3AndPlaysDiscountedOrcFromDiscard() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		var troll = scn.GetShadowCard("troll");
		var orc1 = scn.GetShadowCard("orc1");
		var orc2 = scn.GetShadowCard("orc2");
		var orc3 = scn.GetShadowCard("orc3");
		var orc4 = scn.GetShadowCard("orc4");
		var orc5 = scn.GetShadowCard("orc5");
		scn.ShadowMoveCharToTable(troll);
		scn.ShadowMoveCardToDiscard(orc1, orc2, orc3, orc4, orc5);

		scn.StartGame();

		scn.SetTwilight(17);

		scn.FreepsPassCurrentPhaseAction();

		assertEquals(20, scn.GetTwilight());
		assertTrue(scn.ShadowActionAvailable(troll));
		assertEquals(Zone.DISCARD, orc1.getZone());

		scn.ShadowUseCardAction(troll);
		assertEquals(5, scn.GetShadowCardChoiceCount());
		scn.ShadowChooseCardBPFromSelection(orc1);

		// -3 for ability, -3 for orc, +2 for discount, -2 for roaming
		assertEquals(14, scn.GetTwilight());
		assertEquals(Zone.SHADOW_CHARACTERS, orc1.getZone());
		assertEquals(0, scn.GetWoundsOn(orc1));
	}
}
