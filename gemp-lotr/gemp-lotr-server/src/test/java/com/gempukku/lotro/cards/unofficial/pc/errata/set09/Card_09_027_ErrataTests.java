package com.gempukku.lotro.cards.unofficial.pc.errata.set09;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_09_027_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>()
				{{
					put("sentback", "79_27");
					// put other cards in here as needed for the test case
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void SentBackStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 9
		* Title: Sent Back
		* Unique: False
		* Side: FREE_PEOPLE
		* Culture: Gandalf
		* Twilight Cost: 2
		* Type: condition
		* Subtype: Support Area
		* Game Text: <b>Skirmish:</b> Discard this condition to discard each minion skirmishing your Wizard.
		 * Place that Wizard in your dead pile.
	    * <b>Fellowship</b>: Play a Wizard (even if another copy of that Wizard is in your dead pile).
		*/

		//Pre-game setup
		var scn = GetScenario();

		var sentback = scn.GetFreepsCard("sentback");

		assertFalse(sentback.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, sentback.getBlueprint().getSide());
		assertEquals(Culture.GANDALF, sentback.getBlueprint().getCulture());
		assertEquals(CardType.CONDITION, sentback.getBlueprint().getCardType());
		assertTrue(scn.HasKeyword(sentback, Keyword.SUPPORT_AREA));
		assertEquals(2, sentback.getBlueprint().getTwilightCost());
	}

	//@Test
	public void SentBackTest1() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var card = scn.GetFreepsCard("card");
		scn.FreepsMoveCardToHand(card);

		scn.StartGame();
		scn.FreepsPlayCard(card);

		assertEquals(2, scn.GetTwilight());
	}
}
