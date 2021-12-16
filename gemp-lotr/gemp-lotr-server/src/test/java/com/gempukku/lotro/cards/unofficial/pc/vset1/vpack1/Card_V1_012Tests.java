
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

public class Card_V1_012Tests
{
	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("three", "151_12");
					put("narya", "3_34");
					put("vilya", "3_27");
					put("nenya", "3_23");
					put("gandalf", "1_364");
					put("elrond", "1_40");
					put("galadriel", "1_45");


				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void ThreeRingsStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: *Three Rings for the Elven Kings
		* Side: Free Peoples
		* Culture: elven
		* Twilight Cost: 1
		* Type: condition
		* Subtype: Support Area
		* Game Text: Tale.
		* 	Each time the fellowship moves you may spot an [elven] or [gandalf] ring to place a three from
		 * 	hand beneath your draw deck and draw a three. If you can spot 3 [elven] or [gandalf] rings you
		 * 	may also heal a companion.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl three = scn.GetFreepsCard("three");

		assertTrue(three.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(three, Keyword.SUPPORT_AREA));
		assertTrue(scn.HasKeyword(three, Keyword.TALE));
		assertEquals(1, three.getBlueprint().getTwilightCost());
		assertEquals(CardType.CONDITION, three.getBlueprint().getCardType());
		assertEquals(Culture.ELVEN, three.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, three.getBlueprint().getSide());
	}

	@Test
	public void ThreeRingsSpotsNaryaToMusterUnderDrawDeck() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl three = scn.GetFreepsCard("three");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl narya = scn.GetFreepsCard("narya");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl vilya = scn.GetFreepsCard("vilya");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl nenya = scn.GetFreepsCard("nenya");
		scn.FreepsMoveCardToHand(three, gandalf, narya, elrond, vilya);

		scn.StartGame();
		scn.FreepsPlayCard(three);
		scn.FreepsPlayCard(gandalf);
		scn.FreepsPlayCard(narya);

		assertEquals(2, scn.GetFreepsHandCount());
		assertEquals(2, scn.GetFreepsDeckCount());
		scn.FreepsSkipCurrentPhaseAction();

		assertTrue(scn.FreepsHasOptionalTriggerAvailable());
		scn.FreepsAcceptOptionalTrigger();
		assertEquals(2, scn.GetFreepsCardChoiceCount());
		scn.FreepsChooseCardIDFromSelection(vilya);
		// vilya put on the bottom of the deck, and then drew one card to get back to 2
		assertEquals(2, scn.GetFreepsHandCount());
		assertEquals(2, scn.GetFreepsDeckCount());
		assertEquals(vilya, scn.GetFreepsBottomOfDeck());
		assertFalse(scn.FreepsAnyDecisionsAvailable());
	}

	@Test
	public void ThreeRingsSpotsVilyaToMusterUnderDrawDeck() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl three = scn.GetFreepsCard("three");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl narya = scn.GetFreepsCard("narya");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl vilya = scn.GetFreepsCard("vilya");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl nenya = scn.GetFreepsCard("nenya");
		scn.FreepsMoveCardToHand(three, gandalf, narya, elrond, vilya);

		scn.StartGame();
		scn.FreepsPlayCard(three);
		scn.FreepsPlayCard(gandalf);
		scn.FreepsPlayCard(elrond);
		scn.FreepsPlayCard(vilya);

		assertEquals(1, scn.GetFreepsHandCount());
		assertEquals(2, scn.GetFreepsDeckCount());
		scn.FreepsSkipCurrentPhaseAction();

		assertTrue(scn.FreepsHasOptionalTriggerAvailable());
		scn.FreepsAcceptOptionalTrigger();
		// narya automatically put on the bottom of the deck, and then drew one card to get back to 1
		assertEquals(1, scn.GetFreepsHandCount());
		assertEquals(2, scn.GetFreepsDeckCount());
		assertEquals(narya, scn.GetFreepsBottomOfDeck());
		assertFalse(scn.FreepsAnyDecisionsAvailable());
	}

	@Test
	public void ThreeRingsSpotsThreeRingsToHeal() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl three = scn.GetFreepsCard("three");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl narya = scn.GetFreepsCard("narya");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl vilya = scn.GetFreepsCard("vilya");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl nenya = scn.GetFreepsCard("nenya");
		scn.FreepsMoveCardToHand(three, gandalf, narya, elrond, vilya, galadriel, nenya);

		scn.StartGame();
		scn.FreepsPlayCard(three);
		scn.FreepsPlayCard(gandalf);
		scn.FreepsPlayCard(narya);
		scn.FreepsPlayCard(elrond);
		scn.FreepsPlayCard(vilya);
		scn.FreepsPlayCard(galadriel);
		scn.FreepsPlayCard(nenya);

		scn.AddWoundsToChar(gandalf, 1);
		scn.AddWoundsToChar(scn.GetRingBearer(), 1);

		scn.FreepsSkipCurrentPhaseAction();

		assertTrue(scn.FreepsHasOptionalTriggerAvailable());
		scn.FreepsAcceptOptionalTrigger();

	}
}
