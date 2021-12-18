
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

public class Card_V1_011Tests
{
	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("library", "151_11");
					put("arwen", "1_30");
					put("elrond", "3_13");
					put("galadriel", "1_45");
					put("orophin", "1_56");
					put("tale", "1_66");

					put("runner", "1_178");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void LibraryofRivendellStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: *Library of Rivendell
		* Side: Free Peoples
		* Culture: elven
		* Twilight Cost: 2
		* Type: artifact
		* Subtype: Support Area
		* Game Text: To play spot Elrond or 3 Elves.
		* 	Each companion bearing an [elven] tale is strength +1.
		* 	Skirmish: Discard an [elven] tale from play to make an [elven] companion strength +1.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl library = scn.GetFreepsCard("library");

		assertTrue(library.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(library, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(2, library.getBlueprint().getTwilightCost());
		assertEquals(CardType.ARTIFACT, library.getBlueprint().getCardType());
		assertEquals(Culture.ELVEN, library.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, library.getBlueprint().getSide());
	}

	@Test
	public void LibraryofRivendellCanPlayIfSpot3Elves() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl library = scn.GetFreepsCard("library");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		scn.FreepsMoveCardToHand(library, galadriel, orophin, arwen);

		scn.StartGame();
		assertFalse(scn.FreepsActionAvailable("Library"));
		scn.FreepsPlayCard(arwen);
		assertFalse(scn.FreepsActionAvailable("Library"));
		scn.FreepsPlayCard(orophin);
		assertFalse(scn.FreepsActionAvailable("Library"));
		scn.FreepsPlayCard(galadriel);
		assertTrue(scn.FreepsActionAvailable("Library"));
	}

	@Test
	public void LibraryofRivendellCanPlayIfSpotElrond() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl library = scn.GetFreepsCard("library");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		scn.FreepsMoveCardToHand(library, elrond);

		scn.StartGame();
		assertFalse(scn.FreepsActionAvailable("Library"));
		scn.FreepsPlayCard(elrond);
		assertTrue(scn.FreepsActionAvailable("Library"));
	}

	@Test
	public void LibraryofRivendellPumpsTaleBearers() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl library = scn.GetFreepsCard("library");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl tale = scn.GetFreepsCard("tale");
		scn.FreepsMoveCardToHand(library, tale);
		scn.FreepsMoveCharToTable(arwen, elrond);

		scn.StartGame();
		//decline elrond's text
		scn.FreepsDeclineOptionalTrigger();

		scn.FreepsPlayCard(tale);
		scn.FreepsChooseCard(arwen);
		assertEquals(6, scn.GetStrength(arwen));

		scn.FreepsPlayCard(library);
		assertEquals(7, scn.GetStrength(arwen));
	}

	@Test
	public void LibraryofRivendellBurnsTalesToPumpElfCompanions() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl library = scn.GetFreepsCard("library");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl tale = scn.GetFreepsCard("tale");
		scn.FreepsMoveCardToHand(library, tale);
		scn.FreepsMoveCharToTable(arwen, elrond);

		PhysicalCardImpl runner = scn.GetShadowCard("runner");
		scn.ShadowMoveCharToTable(runner);

		scn.StartGame();
		//decline elrond's text
		scn.FreepsDeclineOptionalTrigger();

		scn.FreepsPlayCard(tale);
		scn.FreepsChooseCard(elrond);
		scn.FreepsPlayCard(library);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.SkipCurrentPhaseActions();
		scn.FreepsAssignToMinions(arwen, runner);
		scn.FreepsResolveSkirmish(arwen);
		assertTrue(scn.FreepsActionAvailable("Library"));
		assertEquals(6, scn.GetStrength(arwen));
		scn.FreepsUseCardAction(library);
		assertEquals(7, scn.GetStrength(arwen));
	}
}
