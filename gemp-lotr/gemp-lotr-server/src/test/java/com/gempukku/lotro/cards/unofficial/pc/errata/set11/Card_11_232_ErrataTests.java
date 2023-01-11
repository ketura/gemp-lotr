package com.gempukku.lotro.cards.unofficial.pc.errata.set11;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_11_232_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("gimli", "1_13");
					put("boromir", "1_97");

					put("soldier1", "1_271");
					put("soldier2", "1_271");
				}},
				new HashMap<>() {{
					put("cavern", "61_232");
					put("site2", "11_237");
					put("site3", "11_237");
					put("site4", "11_237");
					put("site5", "11_237");
					put("site6", "11_237");
					put("site7", "11_237");
					put("site8", "11_237");
					put("site9", "11_237");
				}},
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.ATARRing,
				"open"
		);
	}

	@Test
	public void CavernEntranceStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 11
		* Title: Cavern Entrance
		* Side: Free Peoples
		* Culture: 
		* Twilight Cost: 1
		* Type: site
		* Subtype: Standard
		* Game Text: <b>Underground.</b> At the start of each skirmish, you may exert your character in that skirmish to prevent special abilities being used.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl card = scn.GetFreepsSite("Cavern Entrance");

		assertFalse(card.getBlueprint().isUnique());
		assertEquals(CardType.SITE, card.getBlueprint().getCardType());
		assertTrue(scn.HasKeyword(card, Keyword.UNDERGROUND));
		assertEquals(SitesBlock.SHADOWS, card.getBlueprint().getSiteBlock());
		assertEquals(1, card.getBlueprint().getTwilightCost());
	}

	@Test
	public void CavernEntranceDoesNotBlockIfBothDecline() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
		PhysicalCardImpl cavern = scn.GetShadowSite("Cavern Entrance");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Ettenmoors");
		scn.FreepsMoveCharToTable(gimli, boromir);

		PhysicalCardImpl soldier1 = scn.GetShadowCard("soldier1");
		PhysicalCardImpl soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCharToTable(soldier1, soldier2);

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();

		scn.StartGame();

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowChooseCardBPFromSelection(cavern);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();

		scn.FreepsAssignToMinions(gimli, soldier1);
		scn.ShadowDeclineAssignments();
		scn.FreepsResolveSkirmish(gimli);

		assertTrue(scn.FreepsDecisionAvailable("prevent all special abilities"));
		scn.FreepsChooseNo();
		assertTrue(scn.ShadowDecisionAvailable("prevent all special abilities"));
		scn.ShadowChooseNo();
		assertTrue(scn.FreepsActionAvailable(gimli));
		assertTrue(scn.FreepsActionAvailable(boromir));
		scn.FreepsPassCurrentPhaseAction();
		assertTrue(scn.ShadowActionAvailable(soldier1));
		assertTrue(scn.ShadowActionAvailable(soldier2));
	}

	@Test
	public void CavernEntranceDoesNotBlockOrOfferIfBothExhausted() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
		PhysicalCardImpl cavern = scn.GetShadowSite("Cavern Entrance");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Ettenmoors");
		scn.FreepsMoveCharToTable(gimli, boromir);

		PhysicalCardImpl soldier1 = scn.GetShadowCard("soldier1");
		PhysicalCardImpl soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCharToTable(soldier1, soldier2);

		scn.AddWoundsToChar(gimli, 2);
		scn.AddWoundsToChar(soldier1, 1);

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();

		scn.StartGame();

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowChooseCardBPFromSelection(cavern);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();

		scn.FreepsAssignToMinions(gimli, soldier1);
		scn.ShadowDeclineAssignments();
		scn.FreepsResolveSkirmish(gimli);

		assertFalse(scn.FreepsDecisionAvailable("prevent all special abilities"));
		assertFalse(scn.ShadowDecisionAvailable("prevent all special abilities"));
		assertFalse(scn.FreepsActionAvailable(gimli));
		assertTrue(scn.FreepsActionAvailable(boromir));
		scn.FreepsPassCurrentPhaseAction();
		assertTrue(scn.ShadowActionAvailable(soldier2));
		var choices = scn.ShadowGetCardChoices();
		assertEquals(1, choices.size()); // soldier2 but not soldier1
		assertEquals("" + soldier2.getCardId(), choices.get(0));
		assertTrue(scn.ShadowActionAvailable(soldier2));
	}

	@Test
	public void CavernEntranceBlocksIfFreepsExhaustedButShadowAccepts() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
		PhysicalCardImpl cavern = scn.GetShadowSite("Cavern Entrance");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Ettenmoors");
		scn.FreepsMoveCharToTable(gimli, boromir);

		PhysicalCardImpl soldier1 = scn.GetShadowCard("soldier1");
		PhysicalCardImpl soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCharToTable(soldier1, soldier2);

		scn.AddWoundsToChar(gimli, 2);

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();

		scn.StartGame();

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowChooseCardBPFromSelection(cavern);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();

		scn.FreepsAssignToMinions(gimli, soldier1);
		scn.ShadowDeclineAssignments();
		scn.FreepsResolveSkirmish(gimli);

		assertFalse(scn.FreepsDecisionAvailable("prevent all special abilities"));
		assertTrue(scn.ShadowDecisionAvailable("prevent all special abilities"));
		assertEquals(0, scn.GetWoundsOn(soldier1));
		scn.ShadowChooseYes();
		assertEquals(1, scn.GetWoundsOn(soldier1));
		assertEquals(2, scn.GetWoundsOn(gimli));
		assertFalse(scn.FreepsActionAvailable(gimli));
		assertFalse(scn.FreepsActionAvailable(boromir));
		scn.FreepsPassCurrentPhaseAction();
		assertFalse(scn.ShadowActionAvailable(soldier1));
		assertFalse(scn.ShadowActionAvailable(soldier2));
	}

	@Test
	public void CavernEntranceBlocksIfShadowExhaustedButFreepsAccepts() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
		PhysicalCardImpl cavern = scn.GetShadowSite("Cavern Entrance");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Ettenmoors");
		scn.FreepsMoveCharToTable(gimli, boromir);

		PhysicalCardImpl soldier1 = scn.GetShadowCard("soldier1");
		PhysicalCardImpl soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCharToTable(soldier1, soldier2);

		scn.AddWoundsToChar(soldier1, 1);

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();

		scn.StartGame();

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowChooseCardBPFromSelection(cavern);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();

		scn.FreepsAssignToMinions(gimli, soldier1);
		scn.ShadowDeclineAssignments();
		scn.FreepsResolveSkirmish(gimli);

		assertTrue(scn.FreepsDecisionAvailable("prevent all special abilities"));
		assertEquals(0, scn.GetWoundsOn(gimli));
		scn.FreepsChooseYes();
		assertEquals(1, scn.GetWoundsOn(gimli));
		assertFalse(scn.ShadowDecisionAvailable("prevent all special abilities"));
		assertEquals(1, scn.GetWoundsOn(soldier1));
		assertFalse(scn.FreepsActionAvailable(gimli));
		assertFalse(scn.FreepsActionAvailable(boromir));
		scn.FreepsPassCurrentPhaseAction();
		assertFalse(scn.ShadowActionAvailable(soldier1));
		assertFalse(scn.ShadowActionAvailable(soldier2));
	}

	@Test
	public void CavernEntranceBlocksIfFreepsAcceptsButNotShadow() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
		PhysicalCardImpl cavern = scn.GetShadowSite("Cavern Entrance");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Ettenmoors");
		scn.FreepsMoveCharToTable(gimli, boromir);

		PhysicalCardImpl soldier1 = scn.GetShadowCard("soldier1");
		PhysicalCardImpl soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCharToTable(soldier1, soldier2);

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();

		scn.StartGame();

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowChooseCardBPFromSelection(cavern);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();

		scn.FreepsAssignToMinions(gimli, soldier1);
		scn.ShadowDeclineAssignments();
		scn.FreepsResolveSkirmish(gimli);

		assertTrue(scn.FreepsDecisionAvailable("prevent all special abilities"));
		assertEquals(0, scn.GetWoundsOn(gimli));
		scn.FreepsChooseYes();
		assertEquals(1, scn.GetWoundsOn(gimli));
		assertTrue(scn.ShadowDecisionAvailable("prevent all special abilities"));
		scn.ShadowChooseNo();
		assertEquals(0, scn.GetWoundsOn(soldier1));
		assertFalse(scn.FreepsActionAvailable(gimli));
		assertFalse(scn.FreepsActionAvailable(boromir));
		scn.FreepsPassCurrentPhaseAction();
		assertFalse(scn.ShadowActionAvailable(soldier1));
		assertFalse(scn.ShadowActionAvailable(soldier2));
	}

	@Test
	public void CavernEntranceBlocksIfShadowAcceptsButNotFreeps() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
		PhysicalCardImpl cavern = scn.GetShadowSite("Cavern Entrance");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Ettenmoors");
		scn.FreepsMoveCharToTable(gimli, boromir);

		PhysicalCardImpl soldier1 = scn.GetShadowCard("soldier1");
		PhysicalCardImpl soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCharToTable(soldier1, soldier2);

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();

		scn.StartGame();

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowChooseCardBPFromSelection(cavern);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();

		scn.FreepsAssignToMinions(gimli, soldier1);
		scn.ShadowDeclineAssignments();
		scn.FreepsResolveSkirmish(gimli);

		assertTrue(scn.FreepsDecisionAvailable("prevent all special abilities"));
		scn.FreepsChooseNo();
		assertTrue(scn.ShadowDecisionAvailable("prevent all special abilities"));
		assertEquals(0, scn.GetWoundsOn(soldier1));
		scn.ShadowChooseYes();
		assertEquals(1, scn.GetWoundsOn(soldier1));
		assertEquals(0, scn.GetWoundsOn(gimli));
		assertFalse(scn.FreepsActionAvailable(gimli));
		assertFalse(scn.FreepsActionAvailable(boromir));
		scn.FreepsPassCurrentPhaseAction();
		assertFalse(scn.ShadowActionAvailable(soldier1));
		assertFalse(scn.ShadowActionAvailable(soldier2));
	}

	@Test
	public void CavernEntranceStillOffersIfFreepsAccepts() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
		PhysicalCardImpl cavern = scn.GetShadowSite("Cavern Entrance");
		PhysicalCardImpl site1 = scn.GetFreepsSite("Ettenmoors");
		scn.FreepsMoveCharToTable(gimli, boromir);

		PhysicalCardImpl soldier1 = scn.GetShadowCard("soldier1");
		PhysicalCardImpl soldier2 = scn.GetShadowCard("soldier2");
		scn.ShadowMoveCharToTable(soldier1, soldier2);

		scn.FreepsChooseCardBPFromSelection(site1);
		scn.SkipStartingFellowships();

		scn.StartGame();

		scn.FreepsPassCurrentPhaseAction();
		scn.ShadowChooseCardBPFromSelection(cavern);

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();

		scn.FreepsAssignToMinions(gimli, soldier1);
		scn.ShadowDeclineAssignments();
		scn.FreepsResolveSkirmish(gimli);

		assertTrue(scn.FreepsDecisionAvailable("prevent all special abilities"));
		scn.FreepsChooseYes();
		assertTrue(scn.ShadowDecisionAvailable("prevent all special abilities"));
		assertEquals(0, scn.GetWoundsOn(soldier1));
		scn.ShadowChooseYes();
		assertEquals(1, scn.GetWoundsOn(soldier1));
		assertFalse(scn.FreepsActionAvailable(gimli));
		assertFalse(scn.FreepsActionAvailable(boromir));
		scn.FreepsPassCurrentPhaseAction();
		assertFalse(scn.ShadowActionAvailable(soldier1));
		assertFalse(scn.ShadowActionAvailable(soldier2));
	}
}
