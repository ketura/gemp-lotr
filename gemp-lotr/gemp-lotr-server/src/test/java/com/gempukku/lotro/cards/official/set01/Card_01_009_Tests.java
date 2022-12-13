package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_01_009_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("axe", "1_9");
					put("gimli", "1_13");

					put("runner", "1_178");
					put("runner2", "1_178");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void DwarvenAxeStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: Dwarven Axe
		* Unique: False
		* Side: FREE_PEOPLE
		* Culture: Dwarven
		* Twilight Cost: 0
		* Type: possession
		* Subtype: Hand Weapon
		* Strength: 2
		* Game Text: Bearer must be a Dwarf. <br>Each time a minion loses a skirmish to bearer, that minion's owner must exert a minion or discard the bottom card of their draw deck.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var card = scn.GetFreepsCard("axe");

		assertFalse(card.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, card.getBlueprint().getSide());
		assertEquals(Culture.DWARVEN, card.getBlueprint().getCulture());
		assertEquals(CardType.POSSESSION, card.getBlueprint().getCardType());
		assertTrue(card.getBlueprint().getPossessionClasses().contains(PossessionClass.HAND_WEAPON));
		assertEquals(0, card.getBlueprint().getTwilightCost());
		assertEquals(2, card.getBlueprint().getStrength());
	}

	@Test
	public void AxeMustBeBorneByDwarf() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var axe = scn.GetFreepsCard("axe");
		var gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCardToHand(axe, gimli);

		scn.StartGame();
		assertFalse(scn.FreepsPlayAvailable(axe));
		scn.FreepsPlayCard(gimli);
		assertTrue(scn.FreepsPlayAvailable(axe));
	}

	@Test
	public void AxeCausesDeckDiscardOnSkirmishVictory() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var axe = scn.GetFreepsCard("axe");
		var gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCharToTable(gimli);
		scn.AttachCardsTo(gimli, axe);

		var runner = scn.GetShadowCard("runner");
		scn.ShadowMoveCharToTable(runner);

		scn.StartGame();

		scn.SkipToPhase(Phase.ASSIGNMENT);

		scn.PassCurrentPhaseActions();
		scn.FreepsAssignToMinions(gimli, runner);

		scn.FreepsResolveSkirmish(gimli);
		scn.PassCurrentPhaseActions();

		var topcard = scn.GetShadowTopOfDeck();
		assertEquals(Zone.DECK, topcard.getZone());
		assertEquals(3, scn.GetShadowDeckCount());
		assertEquals(0, scn.GetShadowDiscardCount());

		scn.FreepsResolveActionOrder("Axe");
		assertEquals(Zone.DISCARD, topcard.getZone());
		assertEquals(2, scn.GetShadowDeckCount());
		assertEquals(2, scn.GetShadowDiscardCount());//runner, and a card discarded from the deck

	}

	@Test
	public void AxeAffectsMaxOneMinionDuringSkirmish() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var axe = scn.GetFreepsCard("axe");
		var gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCharToTable(gimli);
		scn.AttachCardsTo(gimli, axe);

		var runner = scn.GetShadowCard("runner");
		var runner2 = scn.GetShadowCard("runner2");
		scn.ShadowMoveCharToTable(runner, runner2);

		scn.StartGame();

		scn.SkipToPhase(Phase.ASSIGNMENT);

		scn.PassCurrentPhaseActions();
		scn.FreepsDeclineAssignments();
		scn.ShadowAssignToMinions(gimli, runner, runner2);

		scn.FreepsResolveSkirmish(gimli);
		scn.FreepsUseCardAction(gimli);
		scn.ShadowPassCurrentPhaseAction();
		scn.FreepsUseCardAction(gimli);
		scn.ShadowPassCurrentPhaseAction();
		scn.FreepsPassCurrentPhaseAction();

		//Should only have "resolve skirmish wounds" and one instance of "dwarven axe required trigger".
		// If there are two axe-triggers, then the card is violating the latest Decipher "clarification":
		//
		//"This card can trigger only once for each Shadow player with a minion in that skirmish,
		// regardless of how many minions that player had."
		assertEquals(3, scn.FreepsGetAvailableActions().size());

		var topcard = scn.GetShadowTopOfDeck();
		assertEquals(Zone.DECK, topcard.getZone());
		assertEquals(2, scn.GetShadowDeckCount());
		assertEquals(0, scn.GetShadowDiscardCount());

		//Ideally we shouldn't have both triggers, but at least we can squelch the results of the second
		scn.FreepsResolveActionOrder("Axe");
		scn.FreepsResolveActionOrder("Axe");
		assertEquals(Zone.DISCARD, topcard.getZone());
		assertEquals(1, scn.GetShadowDeckCount());
		assertEquals(3, scn.GetShadowDiscardCount());//2 dead runners, and a card discarded from the deck
	}
}
