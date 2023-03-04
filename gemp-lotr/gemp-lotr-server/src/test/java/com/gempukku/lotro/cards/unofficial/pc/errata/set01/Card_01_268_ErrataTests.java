package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_01_268_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>()
				{{
					put("inquisitor", "51_268");
					put("filler1", "1_269");
					put("filler2", "1_270");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void OrcInquisitorStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: Orc Inquisitor
		* Unique: False
		* Side: SHADOW
		* Culture: Sauron
		* Twilight Cost: 3
		* Type: minion
		* Subtype: Orc
		* Strength: 9
		* Vitality: 3
		* Site Number: 6
		* Game Text: When you play this minion, you may draw a card.  The Free Peoples player may discard 1 card at random from hand to prevent this.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var inquisitor = scn.GetFreepsCard("inquisitor");

		assertFalse(inquisitor.getBlueprint().isUnique());
		assertEquals(Side.SHADOW, inquisitor.getBlueprint().getSide());
		assertEquals(Culture.SAURON, inquisitor.getBlueprint().getCulture());
		assertEquals(CardType.MINION, inquisitor.getBlueprint().getCardType());
		assertEquals(Race.ORC, inquisitor.getBlueprint().getRace());
		assertEquals(3, inquisitor.getBlueprint().getTwilightCost());
		assertEquals(9, inquisitor.getBlueprint().getStrength());
		assertEquals(3, inquisitor.getBlueprint().getVitality());
		assertEquals(6, inquisitor.getBlueprint().getSiteNumber());
	}

	@Test
	public void OrcInquisitorDraws1CardWhenPlayed() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var inquisitor = scn.GetShadowCard("inquisitor");
		scn.ShadowMoveCardToHand(inquisitor);

		scn.StartGame();
		scn.SetTwilight(10);
		scn.FreepsPassCurrentPhaseAction();

		assertEquals(1, scn.GetShadowHandCount());
		assertEquals(Zone.HAND, inquisitor.getZone());
		scn.ShadowPlayCard(inquisitor);
		scn.ShadowHasOptionalTriggerAvailable();

		assertEquals(2, scn.GetShadowDeckCount());
		scn.ShadowAcceptOptionalTrigger();
		assertEquals(Zone.SHADOW_CHARACTERS, inquisitor.getZone());
		assertEquals(1, scn.GetShadowHandCount());
		assertEquals(1, scn.GetShadowDeckCount());
	}

	@Test
	public void OrcInquisitorDrawCanBePreventedByDiscarding1RandomCardFromHand() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var inquisitor = scn.GetShadowCard("inquisitor");
		scn.ShadowMoveCardToHand(inquisitor);

		scn.FreepsMoveCardToHand("filler1", "filler2");

		scn.StartGame();
		scn.SetTwilight(10);
		scn.FreepsPassCurrentPhaseAction();

		scn.ShadowPlayCard(inquisitor);
		scn.ShadowHasOptionalTriggerAvailable();
		scn.ShadowAcceptOptionalTrigger();

		scn.FreepsChoiceAvailable("Discard 1 card at random from hand");
		assertEquals(2, scn.GetFreepsHandCount());
		assertEquals(0, scn.GetFreepsDiscardCount());
		assertEquals(2, scn.GetShadowDeckCount());
		assertEquals(0, scn.GetShadowHandCount());
		scn.FreepsAcceptOptionalTrigger();
		assertEquals(1, scn.GetFreepsHandCount());
		assertEquals(1, scn.GetFreepsDiscardCount());
		assertEquals(2, scn.GetShadowDeckCount());
		assertEquals(0, scn.GetShadowHandCount());

	}
}
