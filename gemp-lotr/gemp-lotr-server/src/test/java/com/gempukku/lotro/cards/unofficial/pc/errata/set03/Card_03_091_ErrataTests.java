package com.gempukku.lotro.cards.unofficial.pc.errata.set03;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_03_091_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("cruelty", "73_91");
					put("soldier1", "1_271");
					put("soldier2", "1_271");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void HisCrueltyandMaliceStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 3
		* Title: His Cruelty and Malice
		* Unique: False
		* Side: SHADOW
		* Culture: Sauron
		* Twilight Cost: 1
		* Type: condition
		* Subtype: Support Area
		* Game Text: Regroup: Exert or discard a [sauron] minion to exert a companion.  The Free Peoples player may discard the top 2 cards from their draw deck to prevent this.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var cruelty = scn.GetFreepsCard("cruelty");

		assertFalse(cruelty.getBlueprint().isUnique());
		assertEquals(Side.SHADOW, cruelty.getBlueprint().getSide());
		assertEquals(Culture.SAURON, cruelty.getBlueprint().getCulture());
		assertEquals(CardType.CONDITION, cruelty.getBlueprint().getCardType());
		assertTrue(scn.HasKeyword(cruelty, Keyword.SUPPORT_AREA));
		assertEquals(1, cruelty.getBlueprint().getTwilightCost());
	}

	@Test
	public void HisCrueltyandMaliceGivesTheChoiceToExertOrDiscard() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var cruelty = scn.GetShadowCard("cruelty");
		var soldier1 = scn.GetShadowCard("soldier1");
		var soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCardToSupportArea(cruelty);
		scn.ShadowMoveCharToTable(soldier1, soldier2);
		scn.AddWoundsToChar(soldier1, 1);

		scn.StartGame();

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();
		scn.FreepsDeclineAssignments();
		scn.ShadowDeclineAssignments();

		//Regroup phase
		scn.FreepsPassCurrentPhaseAction();
		assertTrue(scn.ShadowActionAvailable(cruelty));
		scn.ShadowUseCardAction(cruelty);
		assertEquals(2, scn.GetShadowCardChoiceCount());
		assertEquals(Zone.SHADOW_CHARACTERS, soldier1.getZone());
		scn.ShadowChooseCard(soldier1);
		assertEquals(Zone.DISCARD, soldier1.getZone());
		assertTrue(scn.FreepsDecisionAvailable("Discard"));
	}
}
