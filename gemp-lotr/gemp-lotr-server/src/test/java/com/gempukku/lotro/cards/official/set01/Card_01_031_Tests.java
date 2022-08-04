
package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_01_031_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("asfaloth", "1_31");
					put("arwen", "3_7");
					put("legolas", "1_50");
					put("erestor", "3_14");
					put("orophin", "1_56");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void AsfalothStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: *Asfaloth
		* Side: Free Peoples
		* Culture: Elven
		* Twilight Cost: 2
		* Type: Possession
		* Item Class: Mount
		* Game Text: Bearer must be an Elf.  When played on Arwen, Asfaloth's twilight cost is -2.  While at a plains
		 * site, bearer is strength +2.  Discard Asfaloth when at an underground site.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl asfaloth = scn.GetFreepsCard("asfaloth");

		assertTrue(asfaloth.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, asfaloth.getBlueprint().getSide());
		assertEquals(Culture.ELVEN, asfaloth.getBlueprint().getCulture());
		assertEquals(CardType.POSSESSION, asfaloth.getBlueprint().getCardType());
		assertEquals(PossessionClass.MOUNT, asfaloth.getBlueprint().getPossessionClasses().stream().findFirst().get());
		assertEquals(2, asfaloth.getBlueprint().getTwilightCost());
		assertEquals(2, asfaloth.getBlueprint().getStrength());
//		assertEquals(3, asfaloth.getBlueprint().getVitality());
		//assertEquals(, asfaloth.getBlueprint().getResistance());
		//assertEquals(Signet., asfaloth.getBlueprint().getSignet());
		//assertEquals(3, asfaloth.getBlueprint().getAllyHomeSiteNumbers()[0]); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void BearerMustBeAnElf() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl asfaloth = scn.GetFreepsCard("asfaloth");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl erestor = scn.GetFreepsCard("erestor");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		scn.FreepsMoveCardToHand(asfaloth, arwen, erestor, orophin);

		scn.StartGame();

		assertFalse(scn.FreepsCardPlayAvailable(asfaloth));
		scn.FreepsPlayCard(orophin);
		assertTrue(scn.FreepsCardPlayAvailable(asfaloth));
		scn.FreepsPlayCard(arwen);
		scn.FreepsPlayCard(asfaloth);
		assertEquals(2, scn.GetFreepsCardChoiceCount()); // can go on either arwen or orophin
	}

	@Test
	public void TwilightCost2OnNonArwen() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl asfaloth = scn.GetFreepsCard("asfaloth");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl erestor = scn.GetFreepsCard("erestor");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		scn.FreepsMoveCharToTable(arwen, erestor, orophin);
		scn.FreepsMoveCardToHand(asfaloth);

		scn.StartGame();

		assertEquals(0, scn.GetTwilight());
		scn.FreepsPlayCard(asfaloth);
		scn.FreepsChooseCard(erestor);
		assertEquals(2, scn.GetTwilight());
	}

	@Test
	public void TwilightCost0OnArwen() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl asfaloth = scn.GetFreepsCard("asfaloth");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl erestor = scn.GetFreepsCard("erestor");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		scn.FreepsMoveCharToTable(arwen, erestor, orophin);
		scn.FreepsMoveCardToHand(asfaloth);

		scn.StartGame();

		assertEquals(0, scn.GetTwilight());
		scn.FreepsPlayCard(asfaloth);
		scn.FreepsChooseCard(arwen);
		assertEquals(0, scn.GetTwilight());
	}

	@Test
	public void BearerIsStrengthPlus2AtPlains() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl asfaloth = scn.GetFreepsCard("asfaloth");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl erestor = scn.GetFreepsCard("erestor");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		scn.FreepsMoveCharToTable(arwen, erestor, orophin);
		scn.FreepsMoveCardToHand(asfaloth);

		//cheating to ensure site 2 qualifies
		scn.ApplyAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.PLAINS));

		scn.StartGame();

		assertEquals(6, scn.GetStrength(arwen));
		scn.FreepsPlayCard(asfaloth);
		scn.FreepsChooseCard(arwen);
		assertEquals(8, scn.GetStrength(arwen));
		scn.FreepsPassCurrentPhaseAction();

		assertEquals(10, scn.GetStrength(arwen));
	}

	@Test
	public void SelfDiscardsAtUnderground() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl asfaloth = scn.GetFreepsCard("asfaloth");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl erestor = scn.GetFreepsCard("erestor");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		scn.FreepsMoveCharToTable(arwen, erestor, orophin);
		scn.FreepsMoveCardToHand(asfaloth);

		//cheating to ensure site 2 qualifies
		scn.ApplyAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.UNDERGROUND));

		scn.StartGame();

		scn.FreepsPlayCard(asfaloth);
		scn.FreepsChooseCard(arwen);
		assertEquals(Zone.ATTACHED, asfaloth.getZone());
		scn.FreepsPassCurrentPhaseAction();

		scn.FreepsChoose("0"); // timing tie between Asfaloth and Site
		assertEquals(Zone.DISCARD, asfaloth.getZone());
	}

	@Test
	public void TransferBetweenAllyAndCompanionPaysTwilight() throws DecisionResultInvalidException, CardNotFoundException {
		// Bug reported: https://play.lotrtcgpc.net/gemp-lotr/game.html?replayId=FallsLover$ilpahhdbsdmp8i2x
		// Site 6 permitted multiple transfers between Rumil and Legolas on site 6 with 0 twilight added to the pool.

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl asfaloth = scn.GetFreepsCard("asfaloth");
		PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
		PhysicalCardImpl legolas = scn.GetFreepsCard("legolas");
		PhysicalCardImpl erestor = scn.GetFreepsCard("erestor");
		PhysicalCardImpl orophin = scn.GetFreepsCard("orophin");
		scn.FreepsMoveCharToTable(arwen, legolas, erestor, orophin);
		scn.FreepsMoveCardToHand(asfaloth);

		scn.ApplyAdHocModifier(new MoveLimitModifier(null, 10));

		scn.StartGame();

		assertEquals(0, scn.GetTwilight());
		scn.FreepsPlayCard(asfaloth);
		scn.FreepsChooseCard(erestor);
		assertEquals(2, scn.GetTwilight());

		scn.SkipToSite(3);

		assertEquals(0, scn.GetTwilight());
		assertTrue(scn.FreepsCardTransferAvailable(asfaloth));
		scn.FreepsTransferCard(asfaloth);
		scn.FreepsChooseCard(legolas);
		assertEquals(2, scn.GetTwilight());
	}

}
