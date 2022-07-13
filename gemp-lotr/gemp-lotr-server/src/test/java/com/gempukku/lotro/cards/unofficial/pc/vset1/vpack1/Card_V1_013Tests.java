
package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_013Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("counsel", "151_7");
					put("elrond", "1_40");
					put("galadriel", "1_45");
					put("orophin", "1_56");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void CounselStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		 * Set: V1
		 * Title: Counsel of the Wise
		 * Side: Free Peoples
		 * Culture: elven
		 * Twilight Cost: 0
		 * Type: event
		 * Subtype: Fellowship
		 * Game Text: Add (X) to take an [elven] ally with a twilight cost of X or less into hand from your draw deck.
		 */

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl counsel = scn.GetFreepsCard("counsel");

		assertFalse(counsel.getBlueprint().isUnique());
		//assertTrue(scn.HasKeyword(counsel, Keyword.TALE));
		assertEquals(0, counsel.getBlueprint().getTwilightCost());
		assertEquals(CardType.EVENT, counsel.getBlueprint().getCardType());
		assertEquals(Culture.ELVEN, counsel.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, counsel.getBlueprint().getSide());
	}

	@Test
	public void CounseloftheWiseChoosing4PermitsTakingElrondIntoHand() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl counsel = scn.GetFreepsCard("counsel");
		scn.FreepsMoveCardToHand(counsel);

		scn.StartGame();

		assertEquals(1, scn.GetFreepsHandCount());
		assertEquals(3, scn.GetFreepsDeckCount());
		assertEquals(0, scn.GetTwilight());

		scn.FreepsPlayCard(counsel);
		scn.FreepsChoose("4");
		assertTrue(scn.FreepsDecisionAvailable("Choose card from deck"));
		// Choices available should be 1 Elrond, 1 Galadriel, 1 Orophin
		assertEquals(3, scn.GetFreepsCardChoiceCount());
		scn.FreepsChooseCardBPFromSelection(scn.GetFreepsCard("elrond"));

		assertEquals(1, scn.GetFreepsHandCount());
		assertEquals(2, scn.GetFreepsDeckCount());
		assertEquals(1, scn.GetFreepsDiscardCount());
		assertEquals(4, scn.GetTwilight());
	}
}



