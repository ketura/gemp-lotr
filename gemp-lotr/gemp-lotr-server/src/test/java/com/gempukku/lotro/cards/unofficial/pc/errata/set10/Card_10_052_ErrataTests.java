package com.gempukku.lotro.cards.unofficial.pc.errata.set10;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Card_10_052_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>()
				{{
					put("underfoot", "80_52");
					// put other cards in here as needed for the test case
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void UnderFootStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 10
		* Title: Under Foot
		* Unique: True
		* Side: SHADOW
		* Culture: Raider
		* Twilight Cost: 1
		* Type: condition
		* Subtype: Support Area
		* Game Text: <b>Shadow:</b> If you have initiative, spot a [raider] Man to reconcile your hand (limit once per phase). At the start of the regroup phase, discard this condition. 
		* 	<b>Skirmish:</b> Discard this condition to make a [raider] Man strength +2.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var underfoot = scn.GetFreepsCard("underfoot");

		assertTrue(underfoot.getBlueprint().isUnique());
		assertEquals(Side.SHADOW, underfoot.getBlueprint().getSide());
		assertEquals(Culture.RAIDER, underfoot.getBlueprint().getCulture());
		assertEquals(CardType.CONDITION, underfoot.getBlueprint().getCardType());;
		assertTrue(scn.HasKeyword(underfoot, Keyword.SUPPORT_AREA));
		assertEquals(1, underfoot.getBlueprint().getTwilightCost());
	}

	//@Test
	public void UnderFootTest1() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var card = scn.GetFreepsCard("card");
		scn.FreepsMoveCardToHand(card);

		scn.StartGame();
		scn.FreepsPlayCard(card);

		assertEquals(1, scn.GetTwilight());
	}
}
