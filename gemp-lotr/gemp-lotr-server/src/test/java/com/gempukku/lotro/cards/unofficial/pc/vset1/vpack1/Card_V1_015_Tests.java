
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

public class Card_V1_015_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("gwaihir", "151_15");
					put("aragorn", "1_89");
					put("gandalf", "151_14");
					put("pathfinder", "1_110");

					put("runner", "1_178");
					put("spear", "1_182");
					put("troll", "1_165");

				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void GwaihirStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: *Gwaihir, Lord of Eagles
		* Side: Free Peoples
		* Culture: gandalf
		* Twilight Cost: 4
		* Type: ally
		* Subtype: Eagle
		* Strength: 8
		* Vitality: 2
		* Site Number: 4
		* Game Text: To play, spot Gandalf.
		* 	Each time you move to an opponent's site, you may heal a companion with the Gandalf signet.
		* 	Regroup: Discard Gwaihir to make the move limit for this turn +1; the Shadow player may take up to 2 Shadow cards into hand from their discard pile.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gwaihir = scn.GetFreepsCard("gwaihir");

		assertTrue(gwaihir.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, gwaihir.getBlueprint().getSide());
		assertEquals(Culture.GANDALF, gwaihir.getBlueprint().getCulture());
		assertEquals(CardType.ALLY, gwaihir.getBlueprint().getCardType());
		assertEquals(Race.EAGLE, gwaihir.getBlueprint().getRace());
		//assertTrue(scn.HasKeyword(gwaihir, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(4, gwaihir.getBlueprint().getTwilightCost());
		assertEquals(8, gwaihir.getBlueprint().getStrength());
		assertEquals(2, gwaihir.getBlueprint().getVitality());
		//assertEquals(, gwaihir.getBlueprint().getResistance());
		//assertEquals(Signet., gwaihir.getBlueprint().getSignet());
		assertEquals(4, gwaihir.getBlueprint().getAllyHomeSiteNumbers()[0]); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void GwaihirSpotsGandalfToPlay() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gwaihir = scn.GetFreepsCard("gwaihir");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		scn.FreepsMoveCardToHand(gwaihir, gandalf);

		scn.StartGame();

		assertFalse(scn.FreepsCardPlayAvailable(gwaihir));
		scn.FreepsPlayCard(gandalf);
		assertTrue(scn.FreepsCardPlayAvailable(gwaihir));

		assertEquals(4, scn.GetTwilight());
		scn.FreepsPlayCard(gwaihir);
		assertEquals(8, scn.GetTwilight());
	}

	@Test
	public void MovingOptionallyHealsGandalfSignetOnOpponentsSite() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gwaihir = scn.GetFreepsCard("gwaihir");
		PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
		PhysicalCardImpl pathfinder = scn.GetFreepsCard("pathfinder");
		scn.FreepsMoveCharToTable(gwaihir, aragorn);
		scn.FreepsMoveCardToHand(pathfinder);

		scn.StartGame();

		scn.AddWoundsToChar(aragorn, 3);

		scn.FreepsPassCurrentPhaseAction();

		assertTrue(scn.FreepsHasOptionalTriggerAvailable());
		scn.FreepsAcceptOptionalTrigger();
		assertEquals(2, scn.GetWoundsOn(aragorn));

		scn.SkipToPhase(Phase.REGROUP);
		scn.FreepsPlayCard(pathfinder);
		scn.ShadowPassCurrentPhaseAction();
		scn.FreepsPassCurrentPhaseAction();

		scn.FreepsChooseToMove();
		assertFalse(scn.FreepsHasOptionalTriggerAvailable());
		assertEquals(2, scn.GetWoundsOn(aragorn));
	}

	@Test
	public void RegroupActionDiscardsToMakeMoveLimitPlus1AndShadowTakesCards() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl gwaihir = scn.GetFreepsCard("gwaihir");
		scn.FreepsMoveCharToTable(gwaihir);

		PhysicalCardImpl runner = scn.GetShadowCard("runner");
		PhysicalCardImpl spear = scn.GetShadowCard("spear");
		PhysicalCardImpl troll = scn.GetShadowCard("troll");

		scn.ShadowMoveCardToDiscard(runner, spear, troll);

		scn.StartGame();

		scn.SkipToPhase(Phase.REGROUP);

		assertTrue(scn.FreepsCardActionAvailable(gwaihir));
		assertEquals(Zone.FREE_CHARACTERS, gwaihir.getZone());
		assertEquals(Zone.DISCARD, runner.getZone());
		assertEquals(Zone.DISCARD, spear.getZone());
		assertEquals(Zone.DISCARD, troll.getZone());

		scn.FreepsUseCardAction(gwaihir);

		assertTrue(scn.ShadowDecisionAvailable("Would you like to take up to 2 Shadow cards into hand from discard?"));
		scn.ShadowChooseYes();
		assertTrue(scn.ShadowDecisionAvailable("Choose card from discard"));
		scn.ShadowChooseCardBPFromSelection(runner, troll);

		assertEquals(Zone.DISCARD, gwaihir.getZone());
		assertEquals(Zone.HAND, runner.getZone());
		assertEquals(Zone.DISCARD, spear.getZone());
		assertEquals(Zone.HAND, troll.getZone());
	}

}
