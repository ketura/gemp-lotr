package com.gempukku.lotro.cards.unofficial.pc.errata.set02;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_02_059_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("things", "72_59");
					put("things2", "72_59");
					put("runner", "1_178");
					put("runner2", "1_178");
					put("armory", "1_173");
					put("host", "1_187");
					put("scimitar", "1_180");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void FoulThingsStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 2
		* Title: Foul Things
		* Unique: False
		* Side: SHADOW
		* Culture: Moria
		* Twilight Cost: 2
		* Type: event
		* Subtype: 
		* Game Text: <b>Shadow:</b> Play a [moria] card from your discard pile.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var things = scn.GetFreepsCard("things");

		assertFalse(things.getBlueprint().isUnique());
		assertEquals(Side.SHADOW, things.getBlueprint().getSide());
		assertEquals(Culture.MORIA, things.getBlueprint().getCulture());
		assertEquals(CardType.EVENT, things.getBlueprint().getCardType());
		assertEquals(2, things.getBlueprint().getTwilightCost());
	}

	@Test
	public void FoulThingsCanPlayOtherMoriaCardsFromDiscard() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var things = scn.GetShadowCard("things");
		scn.ShadowMoveCardToHand(things);
		scn.ShadowMoveCardToDiscard("runner2", "armory", "host", "scimitar");
		scn.ShadowMoveCharToTable("runner");

		scn.StartGame();
		scn.SetTwilight(20);
		scn.FreepsPassCurrentPhaseAction();

		assertTrue(scn.ShadowPlayAvailable(things));
		scn.ShadowPlayCard(things);
		assertEquals(4, scn.GetShadowCardChoiceCount());
	}

	@Test
	public void FoulThingsDoesNotCauseInfiniteLoopIfAnotherFoulThingsIsInDiscard() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var things = scn.GetShadowCard("things");
		scn.ShadowMoveCardToHand(things);
		scn.ShadowMoveCardToDiscard("things2", "runner");

		scn.StartGame();
		scn.FreepsPassCurrentPhaseAction();

		assertTrue(scn.ShadowPlayAvailable(things));
	}
}
