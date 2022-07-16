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

		PhysicalCardImpl card = scn.GetFreepsCard("card");

		assertTrue(card.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(card, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(1, card.getBlueprint().getTwilightCost());
		//assertEquals(, card.getBlueprint().getStrength());
		//assertEquals(, card.getBlueprint().getVitality());
		//assertEquals(, card.getBlueprint().getResistance());
		//assertEquals(Signet., card.getBlueprint().getSignet());
		//assertEquals(, card.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies
		assertEquals(CardType.CONDITION, card.getBlueprint().getCardType());
		assertEquals(Culture.GANDALF, card.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, card.getBlueprint().getSide());
	}

	@Test
	public void LamentStacksAtTheStartOfRegroup() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl card = scn.GetFreepsCard("card");
		scn.FreepsMoveCardToHand(card);

		scn.StartGame();
		scn.FreepsPlayCard(card);

		assertEquals(1, scn.GetTwilight());
	}
}
