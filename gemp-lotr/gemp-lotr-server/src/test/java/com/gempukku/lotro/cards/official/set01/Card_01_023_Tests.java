package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_01_023_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>()
				{{
					put("nobody", "1_23");
					put("gimli", "1_13");

					put("uruk", "1_154");
					put("deck1", "1_126");
					put("deck2", "1_127");
					put("deck3", "1_128");
					put("deck4", "1_129");
					put("deck5", "1_130");
					put("deck6", "1_131");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void NobodyTossesaDwarfStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: Nobody Tosses a Dwarf
		* Unique: False
		* Side: FREE_PEOPLE
		* Culture: Dwarven
		* Twilight Cost: 0
		* Type: event
		* Subtype: Response
		* Game Text: If a Dwarf wins a skirmish, discard a Shadow card.  Its owner may discard 3 cards from the bottom of their draw deck to prevent this.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var nobody = scn.GetFreepsCard("nobody");

		assertFalse(nobody.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, nobody.getBlueprint().getSide());
		assertEquals(Culture.DWARVEN, nobody.getBlueprint().getCulture());
		assertEquals(CardType.EVENT, nobody.getBlueprint().getCardType());
		assertTrue(scn.HasKeyword(nobody, Keyword.RESPONSE));
		assertEquals(0, nobody.getBlueprint().getTwilightCost());
	}

	@Test
	public void NobodyDoesNotTriggerIfSkirmishLost() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var nobody = scn.GetFreepsCard("nobody");
		var gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCardToHand(nobody);
		scn.FreepsMoveCharToTable(gimli);

		var uruk = scn.GetShadowCard("uruk");
		scn.ShadowMoveCharToTable(uruk);

		scn.StartGame();

		scn.SkipToAssignments();
		scn.FreepsAssignToMinions(gimli, uruk);
		scn.FreepsResolveSkirmish(gimli);
		scn.PassCurrentPhaseActions();
		assertFalse(scn.FreepsHasOptionalTriggerAvailable());
	}

	@Test
	public void NobodyTriggerDiscardsShadowTopDecks() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var nobody = scn.GetFreepsCard("nobody");
		var gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCardToHand(nobody);
		scn.FreepsMoveCharToTable(gimli);

		var uruk = scn.GetShadowCard("uruk");
		scn.ShadowMoveCharToTable(uruk);
		scn.StartGame();

		scn.SkipToAssignments();
		scn.FreepsAssignToMinions(gimli, uruk);
		scn.FreepsResolveSkirmish(gimli);
		//pump gimli to beat the uruk
		scn.FreepsUseCardAction(gimli);
		scn.ShadowPassCurrentPhaseAction();
		scn.FreepsPassCurrentPhaseAction();

		var top1 = scn.GetFromTopOfShadowDeck(1);
		var top2 = scn.GetFromTopOfShadowDeck(2);
		var top3 = scn.GetFromTopOfShadowDeck(3);
		var top4 = scn.GetFromTopOfShadowDeck(4);

		assertEquals(Zone.DECK, top1.getZone());
		assertEquals(Zone.DECK, top2.getZone());
		assertEquals(Zone.DECK, top3.getZone());
		assertEquals(Zone.DECK, top4.getZone());

		assertTrue(scn.FreepsHasOptionalTriggerAvailable());
		scn.FreepsAcceptOptionalTrigger();

		assertEquals(Zone.DISCARD, top1.getZone());
		assertEquals(Zone.DISCARD, top2.getZone());
		assertEquals(Zone.DISCARD, top3.getZone());
		assertEquals(Zone.DECK,    top4.getZone());
	}

}
