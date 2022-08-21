
package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_01_087_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>() {{
					put("late", "1_87");
					put("gandalf", "1_72");
					put("bb", "1_70");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void AWizardIsNeverLateStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: A Wizard Is Never Late
		* Side: Free Peoples
		* Culture: Gandalf
		* Twilight Cost: 1
		* Type: Event
		* Subtype: Fellowship
		* Game Text: Fellowship: Play a [GANDALF] character from your draw deck.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl late = scn.GetFreepsCard("late");

		assertFalse(late.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, late.getBlueprint().getSide());
		assertEquals(Culture.GANDALF, late.getBlueprint().getCulture());
		assertEquals(CardType.EVENT, late.getBlueprint().getCardType());
		assertTrue(scn.HasKeyword(late, Keyword.FELLOWSHIP)); // test for keywords as needed
		assertEquals(1, late.getBlueprint().getTwilightCost());
//		assertEquals(2, late.getBlueprint().getStrength());
//		assertEquals(3, late.getBlueprint().getVitality());
		//assertEquals(, late.getBlueprint().getResistance());
		//assertEquals(Signet., late.getBlueprint().getSignet());
		//assertEquals(3, late.getBlueprint().getAllyHomeSiteNumbers()[0]); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void PlaysAGandalfCompanionFromDeck() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl late = scn.GetFreepsCard("late");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl bb = scn.GetFreepsCard("bb");
		scn.FreepsMoveCardToHand(late);
		scn.FreepsMoveCardsToTopOfDeck(gandalf, bb);

		scn.StartGame();

		assertTrue(scn.FreepsPlayAvailable(late));
		assertEquals(Zone.DECK, gandalf.getZone());
		assertEquals(Zone.DECK, bb.getZone());

		scn.FreepsPlayCard(late);
		assertTrue(scn.FreepsDecisionAvailable("Choose card from deck"));
		assertEquals(2, scn.GetFreepsCardChoiceCount());
		scn.FreepsChooseCardBPFromSelection(gandalf);

		assertEquals(Zone.FREE_CHARACTERS, gandalf.getZone());
		assertEquals(Zone.DECK, bb.getZone());
	}

	@Test
	public void PlaysAGandalfAllyFromDeck() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl late = scn.GetFreepsCard("late");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl bb = scn.GetFreepsCard("bb");
		scn.FreepsMoveCardToHand(late);
		scn.FreepsMoveCardsToTopOfDeck(gandalf, bb);

		scn.StartGame();

		assertTrue(scn.FreepsPlayAvailable(late));
		assertEquals(Zone.DECK, gandalf.getZone());
		assertEquals(Zone.DECK, bb.getZone());

		scn.FreepsPlayCard(late);
		assertTrue(scn.FreepsDecisionAvailable("Choose card from deck"));
		assertEquals(2, scn.GetFreepsCardChoiceCount());
		scn.FreepsChooseCardBPFromSelection(bb);

		assertEquals(Zone.DECK, gandalf.getZone());
		assertEquals(Zone.SUPPORT, bb.getZone());
	}

	@Test
	public void CanPlayWithNoGandalfCharactersInTheDeck() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl late = scn.GetFreepsCard("late");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl bb = scn.GetFreepsCard("bb");
		scn.FreepsMoveCardToHand(late, gandalf, bb);

		scn.StartGame();

		assertTrue(scn.FreepsPlayAvailable(late));
		assertEquals(Zone.HAND, gandalf.getZone());
		assertEquals(Zone.HAND, bb.getZone());

		scn.FreepsPlayCard(late);

		assertEquals(Zone.DISCARD, late.getZone());
	}
}
