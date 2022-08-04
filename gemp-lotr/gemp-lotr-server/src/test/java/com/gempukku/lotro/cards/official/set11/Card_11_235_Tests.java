
package com.gempukku.lotro.cards.official.set11;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_11_235_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("dontlook", "6_39");
					put("smeagol", "7_71");
					put("boat", "13_48");
					put("slippery", "6_43");
					put("clever", "7_54");
				}},
				new HashMap<String, String>() {{
					put("site1", "11_235");
					put("site2", "18_138");
					put("site3", "18_138");
					put("site4", "18_138");
					put("site5", "18_138");
					put("site6", "18_138");
					put("site7", "18_138");
					put("site8", "18_138");
					put("site9", "18_138");
				}},
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing,
				"open"
		);
	}

	@Test
	public void DammedGateStreamStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 11
		* Title: Dammed Gate-stream
		* Side: Site
		* Culture: Site
		* Twilight Cost: 3
		* Type: Site
		* Site Number: *
		* Game Text: Marsh. At the start of your fellowship phase, you may play a [GOLLUM] Free Peoples card from your draw deck.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl site1 = scn.GetFreepsSite("Dammed Gate-stream");

		assertFalse(site1.getBlueprint().isUnique());
		//assertEquals(Side.FREE_PEOPLE, site1.getBlueprint().getSide());
		//assertEquals(Culture., site1.getBlueprint().getCulture());
		assertEquals(CardType.SITE, site1.getBlueprint().getCardType());
		//assertEquals(Race.CREATURE, site1.getBlueprint().getRace());
		assertTrue(scn.HasKeyword(site1, Keyword.MARSH)); // test for keywords as needed
		assertEquals(3, site1.getBlueprint().getTwilightCost());
		//assertEquals(, site1.getBlueprint().getStrength());
		//assertEquals(, site1.getBlueprint().getVitality());
		//assertEquals(, site1.getBlueprint().getResistance());
		//assertEquals(Signet., site1.getBlueprint().getSignet());
		assertEquals(0, site1.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void StartOfFellowshipPhaseActionAllowsPlayFromDrawDeck() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl frodo = scn.GetRingBearer();
		PhysicalCardImpl dontlook = scn.GetFreepsCard("dontlook");
		PhysicalCardImpl smeagol = scn.GetFreepsCard("smeagol");
		PhysicalCardImpl slippery = scn.GetFreepsCard("slippery");
		PhysicalCardImpl boat = scn.GetFreepsCard("boat");
		PhysicalCardImpl clever = scn.GetFreepsCard("clever");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Dammed Gate-stream");

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();
		//Apparently it's drawing cards now.  It doesn't do this in other formats.
		scn.FreepsMoveCardsToBottomOfDeck(dontlook, smeagol, slippery, boat, clever);
		scn.StartGame();

		assertTrue(scn.FreepsHasOptionalTriggerAvailable());
		scn.FreepsAcceptOptionalTrigger();

		//Not Listening is a response event, it shouldn't show up
		//Clever Hobbits is a skirmish event, also shouldn't show
		//Smeagol and Don't Look At Them are valid.  Fishing Boat would theoretically be valid if Smeagol were in play.
		assertEquals(2, scn.GetFreepsCardChoiceCount());

		assertEquals(Zone.DECK, dontlook.getZone());
		assertEquals(Zone.DECK, smeagol.getZone());
		scn.FreepsChooseCardBPFromSelection(dontlook);

		assertEquals(Zone.SUPPORT, dontlook.getZone());
		assertEquals(Zone.DECK, smeagol.getZone());

	}

}
