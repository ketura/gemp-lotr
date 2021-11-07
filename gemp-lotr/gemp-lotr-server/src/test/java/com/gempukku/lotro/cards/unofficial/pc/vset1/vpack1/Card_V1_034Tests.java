
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

public class Card_V1_034Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("darkwaters", "151_34");
					put("ftentacle1", "2_58");
					put("ftentacle2", "2_58");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void DarkWatersStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: Out of Dark Waters
		* Side: Free Peoples
		* Culture: moria
		* Twilight Cost: 0
		* Type: condition
		* Subtype: Support Area
		* Game Text: Shadow: Remove (1) to stack a tentacle from hand here.
		* 	Shadow: Remove (1) to play a tentacle from here as if from hand.
		* 	Response: If this condition is about to be discarded, discard a tentacle stacked here to prevent that.  
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darkwaters = scn.GetFreepsCard("darkwaters");

		assertFalse(darkwaters.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(darkwaters, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(0, darkwaters.getBlueprint().getTwilightCost());
		assertEquals(CardType.CONDITION, darkwaters.getBlueprint().getCardType());
		assertEquals(Culture.MORIA, darkwaters.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, darkwaters.getBlueprint().getSide());
	}

	@Test
	public void DarkWatersCanAdd1ToStackOrPlayTentacles() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darkwaters = scn.GetShadowCard("darkwaters");
		PhysicalCardImpl ftentacle1 = scn.GetShadowCard("ftentacle1");
		PhysicalCardImpl ftentacle2 = scn.GetShadowCard("ftentacle2");
		scn.ShadowMoveCardToHand(darkwaters, ftentacle1, ftentacle2);

		scn.StartGame();
		scn.SetTwilight(4);
		scn.ApplyAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.MARSH));
		scn.FreepsSkipCurrentPhaseAction();

		scn.ShadowPlayCard(darkwaters);

		assertTrue(scn.ShadowActionAvailable("Dark Waters"));
		assertEquals(7, scn.GetTwilight());
		scn.ShadowUseCardAction(darkwaters);
		scn.ShadowChooseCard(ftentacle1);

		assertEquals(6, scn.GetTwilight());
		assertTrue(scn.ShadowActionAvailable("Dark Waters"));
		scn.ShadowUseCardAction(darkwaters);
		scn.ShadowUseCardAction(darkwaters);
	}
}
