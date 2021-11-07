
package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_033Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("ttent", "151_33");
					put("spear", "1_182");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void ThrashingTentacleStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: Thrashing Tentacle
		* Side: Free Peoples
		* Culture: moria
		* Twilight Cost: 2
		* Type: minion
		* Subtype: Creature
		* Strength: 5
		* Vitality: 2
		* Site Number: 4
		* Game Text: Tentacle. This minion may not bear possessions and is discarded if not at a marsh.
		* 	Shadow: Spot 3 [moria] creatures to play this minion from your discard pile.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ttent = scn.GetFreepsCard("ttent");

		assertFalse(ttent.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(ttent, Keyword.TENTACLE)); // test for keywords as needed
		assertEquals(2, ttent.getBlueprint().getTwilightCost());
		assertEquals(5, ttent.getBlueprint().getStrength());
		assertEquals(2, ttent.getBlueprint().getVitality());
		assertEquals(4, ttent.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies
		assertEquals(CardType.MINION, ttent.getBlueprint().getCardType());
		assertEquals(Race.CREATURE, ttent.getBlueprint().getRace());
		assertEquals(Culture.MORIA, ttent.getBlueprint().getCulture());
		assertEquals(Side.SHADOW, ttent.getBlueprint().getSide());
	}

	@Test
	public void ThrashingTentacleCannotBearPossessions() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ttent = scn.GetShadowCard("ttent");
		PhysicalCardImpl spear = scn.GetShadowCard("spear");
		scn.ShadowMoveCardToHand(ttent, spear);

		scn.StartGame();
		scn.SetTwilight(3);
		scn.ApplyAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.MARSH));
		scn.FreepsSkipCurrentPhaseAction();
		scn.ShadowPlayCard(ttent);

		assertFalse(scn.ShadowActionAvailable("Goblin Spear"));
	}

	@Test
	public void ThrashingTentacleSelfDiscardsIfNotAtAMarsh() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl ttent = scn.GetShadowCard("ttent");
		scn.ShadowMoveCardToHand(ttent);

		scn.StartGame();
		scn.SetTwilight(2);
		scn.FreepsSkipCurrentPhaseAction();
		scn.ShadowPlayCard(ttent);

		assertEquals(1, scn.GetShadowDiscardCount());
	}
}
