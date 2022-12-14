package com.gempukku.lotro.cards.official.set17;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_17_015_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("light", "17_15");
					put("gandalf", "1_72");

					put("runner1", "1_191");
					put("runner2", "1_191");
					put("runner3", "1_191");
					put("scout", "1_178");
					put("balrog", "19_18");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	// Uncomment both @Test markers below once this is ready to be used

	@Test
	public void ANewLightStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 17
		* Title: A New Light
		* Unique: False
		* Side: FREE_PEOPLE
		* Culture: Gandalf
		* Twilight Cost: 1
		* Type: event
		* Subtype: Fellowship
		* Game Text: <b>Spell.</b>
		* 	Spot a [gandalf] Wizard to return a minion to its owner's hand from its owner's discard pile.
		 * 	Reveal that Shadow player's hand and discard a minion from his or her hand.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var light = scn.GetFreepsCard("light");

		assertFalse(light.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, light.getBlueprint().getSide());
		assertEquals(Culture.GANDALF, light.getBlueprint().getCulture());
		assertEquals(CardType.EVENT, light.getBlueprint().getCardType());
		assertTrue(scn.HasKeyword(light, Keyword.FELLOWSHIP));
		assertTrue(scn.HasKeyword(light, Keyword.SPELL));
		assertEquals(1, light.getBlueprint().getTwilightCost());
	}

	@Test
	public void ANewLightFunctions() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var light = scn.GetFreepsCard("light");
		var gandalf = scn.GetFreepsCard("gandalf");
		scn.FreepsMoveCardToHand(light, gandalf);

		var runner1 = scn.GetShadowCard("runner1");
		var runner2 = scn.GetShadowCard("runner2");
		var runner3 = scn.GetShadowCard("runner3");
		var scout = scn.GetShadowCard("scout");
		var balrog = scn.GetShadowCard("balrog");

		scn.ShadowMoveCardToDiscard(runner1, runner2, scout);
		scn.ShadowMoveCardToHand(runner3, balrog);

		scn.StartGame();
		assertFalse(scn.FreepsPlayAvailable(light));
		scn.FreepsPlayCard(gandalf);
		assertTrue(scn.FreepsPlayAvailable(light));
		scn.FreepsPlayCard(light);

		assertTrue(scn.FreepsDecisionAvailable("choose card from discard"));
		assertEquals(2, scn.GetShadowHandCount());
		assertEquals(Zone.DISCARD, scout.getZone());

		scn.FreepsChooseCardBPFromSelection(scout);

		assertEquals(3, scn.GetShadowHandCount());
		assertEquals(Zone.HAND, scout.getZone());

		assertTrue(scn.FreepsDecisionAvailable("hand"));
		scn.FreepsDismissRevealedCards();
		scn.ShadowDismissRevealedCards();

		assertTrue(scn.FreepsDecisionAvailable("choose cards from hand"));
		assertEquals(3, scn.GetShadowHandCount());
		assertEquals(2, scn.GetShadowDiscardCount());
		assertEquals(Zone.HAND, balrog.getZone());
		scn.FreepsChooseCardBPFromSelection(balrog);
		assertEquals(2, scn.GetShadowHandCount());
		assertEquals(3, scn.GetShadowDiscardCount());
		assertEquals(Zone.DISCARD, balrog.getZone());
	}
}
