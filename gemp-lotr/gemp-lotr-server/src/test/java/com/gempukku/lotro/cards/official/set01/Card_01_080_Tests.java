package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_01_080_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("ottar", "1_80");
					put("gandalf", "1_72");
					put("chaff1", "1_80");
					put("chaff2", "1_80");
					put("chaff3", "1_80");
					put("chaff4", "1_80");
					put("chaff5", "1_80");
					put("chaff6", "1_80");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void OttarStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: *Ottar, Man of Laketown
		* Side: Free Peoples
		* Culture: Gandalf
		* Twilight Cost: 1
		* Type: Ally
		* Subtype: Man
		* Strength: 2
		* Vitality: 2
		* Site Number: 3
		* Game Text: To play, spot Gandalf.
		* 	<b>Fellowship:</b> Exert Ottar and discard a card from hand to draw a card.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ottar = scn.GetFreepsCard("ottar");

		assertTrue(ottar.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, ottar.getBlueprint().getSide());
		assertEquals(Culture.GANDALF, ottar.getBlueprint().getCulture());
		assertEquals(CardType.ALLY, ottar.getBlueprint().getCardType());
		assertEquals(Race.MAN, ottar.getBlueprint().getRace());
		assertEquals(1, ottar.getBlueprint().getTwilightCost());
		assertEquals(2, ottar.getBlueprint().getStrength());
		assertEquals(2, ottar.getBlueprint().getVitality());
		//assertEquals(, ottar.getBlueprint().getResistance());
		//assertEquals(Signet., ottar.getBlueprint().getSignet());
		assertEquals(3, ottar.getBlueprint().getAllyHomeSiteNumbers()[0]); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void OttarRequiresGandalfToPlay() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ottar = scn.GetFreepsCard("ottar");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		scn.FreepsMoveCardToHand(ottar, gandalf);

		scn.StartGame();
		assertFalse(scn.FreepsPlayAvailable(ottar));
		scn.FreepsPlayCard(gandalf);
		assertTrue(scn.FreepsPlayAvailable(ottar));
	}

	@Test
	public void OttarExertsAndDiscardsOneCardFromHandToDrawOne() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ottar = scn.GetFreepsCard("ottar");
		PhysicalCardImpl chaff1 = scn.GetFreepsCard("chaff1");
		PhysicalCardImpl chaff2 = scn.GetFreepsCard("chaff2");
		PhysicalCardImpl chaff3 = scn.GetFreepsCard("chaff3");
		PhysicalCardImpl chaff4 = scn.GetFreepsCard("chaff4");
		PhysicalCardImpl chaff5 = scn.GetFreepsCard("chaff5");
		PhysicalCardImpl chaff6 = scn.GetFreepsCard("chaff6");
		scn.FreepsMoveCardToHand(chaff1, chaff2, chaff3);
		scn.FreepsMoveCharToTable(ottar);
		scn.FreepsMoveCardsToTopOfDeck(chaff4, chaff5, chaff6);

		scn.StartGame();

		assertEquals(0, scn.GetWoundsOn(ottar));
		assertEquals(3, scn.GetFreepsHandCount());
		assertTrue(scn.FreepsActionAvailable(ottar));

		scn.FreepsUseCardAction(ottar);

		assertTrue(scn.FreepsDecisionAvailable("Choose cards from hand to discard"));
		assertEquals("0", scn.FreepsGetADParam("min")[0]);
		assertEquals("3", scn.FreepsGetADParam("max")[0]);
		assertEquals(Zone.HAND, chaff1.getZone());
		assertEquals(3, scn.GetFreepsHandCount());

		scn.FreepsChooseCard(chaff1);

		assertEquals(1, scn.GetWoundsOn(ottar));
		assertEquals(Zone.DISCARD, chaff1.getZone());
		assertEquals(3, scn.GetFreepsHandCount()); // Discarded 1, drew 1
	}

	@Test
	public void OttarExertsAndDiscardsTwoCardsFromHandToDrawTwo() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ottar = scn.GetFreepsCard("ottar");
		PhysicalCardImpl chaff1 = scn.GetFreepsCard("chaff1");
		PhysicalCardImpl chaff2 = scn.GetFreepsCard("chaff2");
		PhysicalCardImpl chaff3 = scn.GetFreepsCard("chaff3");
		PhysicalCardImpl chaff4 = scn.GetFreepsCard("chaff4");
		PhysicalCardImpl chaff5 = scn.GetFreepsCard("chaff5");
		PhysicalCardImpl chaff6 = scn.GetFreepsCard("chaff6");
		scn.FreepsMoveCardToHand(chaff1, chaff2, chaff3);
		scn.FreepsMoveCharToTable(ottar);
		scn.FreepsMoveCardsToTopOfDeck(chaff4, chaff5, chaff6);

		scn.StartGame();

		assertEquals(0, scn.GetWoundsOn(ottar));
		assertEquals(3, scn.GetFreepsHandCount());
		assertTrue(scn.FreepsActionAvailable(ottar));

		scn.FreepsUseCardAction(ottar);

		assertTrue(scn.FreepsDecisionAvailable("Choose cards from hand to discard"));
		assertEquals("0", scn.FreepsGetADParam("min")[0]);
		assertEquals("3", scn.FreepsGetADParam("max")[0]);
		assertEquals(Zone.HAND, chaff1.getZone());
		assertEquals(Zone.HAND, chaff2.getZone());
		assertEquals(3, scn.GetFreepsHandCount());

		scn.FreepsChooseCards(chaff1, chaff2);

		assertEquals(1, scn.GetWoundsOn(ottar));
		assertEquals(Zone.DISCARD, chaff1.getZone());
		assertEquals(Zone.DISCARD, chaff2.getZone());
		assertEquals(3, scn.GetFreepsHandCount()); // Discarded 2, drew 2
	}

	@Test
	public void OttarExertsAndDiscardsThreeCardsFromHandToDrawThree() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ottar = scn.GetFreepsCard("ottar");
		PhysicalCardImpl chaff1 = scn.GetFreepsCard("chaff1");
		PhysicalCardImpl chaff2 = scn.GetFreepsCard("chaff2");
		PhysicalCardImpl chaff3 = scn.GetFreepsCard("chaff3");
		PhysicalCardImpl chaff4 = scn.GetFreepsCard("chaff4");
		PhysicalCardImpl chaff5 = scn.GetFreepsCard("chaff5");
		PhysicalCardImpl chaff6 = scn.GetFreepsCard("chaff6");
		scn.FreepsMoveCardToHand(chaff1, chaff2, chaff3);
		scn.FreepsMoveCharToTable(ottar);
		scn.FreepsMoveCardsToTopOfDeck(chaff4, chaff5, chaff6);

		scn.StartGame();

		assertEquals(0, scn.GetWoundsOn(ottar));
		assertEquals(3, scn.GetFreepsHandCount());
		assertTrue(scn.FreepsActionAvailable(ottar));

		scn.FreepsUseCardAction(ottar);

		assertTrue(scn.FreepsDecisionAvailable("Choose cards from hand to discard"));
		assertEquals("0", scn.FreepsGetADParam("min")[0]);
		assertEquals("3", scn.FreepsGetADParam("max")[0]);
		assertEquals(Zone.HAND, chaff1.getZone());
		assertEquals(Zone.HAND, chaff2.getZone());
		assertEquals(Zone.HAND, chaff3.getZone());
		assertEquals(3, scn.GetFreepsHandCount());

		scn.FreepsChooseCards(chaff1, chaff2, chaff3);

		assertEquals(1, scn.GetWoundsOn(ottar));
		assertEquals(Zone.DISCARD, chaff1.getZone());
		assertEquals(Zone.DISCARD, chaff2.getZone());
		assertEquals(Zone.DISCARD, chaff3.getZone());
		assertEquals(3, scn.GetFreepsHandCount()); // Discarded 3, drew 3
	}

	@Test
	public void OttarDoesNotRequireCardInHandToDraw() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ottar = scn.GetFreepsCard("ottar");
		PhysicalCardImpl chaff1 = scn.GetFreepsCard("chaff1");
		PhysicalCardImpl chaff2 = scn.GetFreepsCard("chaff2");
		scn.FreepsMoveCharToTable(ottar);

		scn.StartGame();

		assertEquals(0, scn.GetWoundsOn(ottar));
		assertEquals(0, scn.GetFreepsHandCount());
		assertTrue(scn.FreepsActionAvailable(ottar));
	}
}
