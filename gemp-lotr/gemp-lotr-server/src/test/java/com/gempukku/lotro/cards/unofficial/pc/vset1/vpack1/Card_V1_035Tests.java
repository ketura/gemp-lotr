
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

public class Card_V1_035Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("darkness", "151_35");
					put("balrog", "2_51");
					put("whip", "2_74");
					put("spear", "1_182");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void DarknessStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: The Darkness Grew
		* Side: Free Peoples
		* Culture: moria
		* Twilight Cost: 1
		* Type: condition
		* Subtype: Support Area
		* Game Text: Shadow: Stack a [moria] possession or artifact here.
		* 	Shadow: Spot The Balrog to take a [moria] card stacked here into hand.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darkness = scn.GetFreepsCard("darkness");

		assertFalse(darkness.getBlueprint().isUnique());
		assertTrue(scn.HasKeyword(darkness, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(1, darkness.getBlueprint().getTwilightCost());
		assertEquals(CardType.CONDITION, darkness.getBlueprint().getCardType());
		assertEquals(Culture.MORIA, darkness.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, darkness.getBlueprint().getSide());
	}

	@Test
	public void TheDarknessGrewTest1() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl darkness = scn.GetShadowCard("darkness");
		PhysicalCardImpl balrog = scn.GetShadowCard("balrog");
		PhysicalCardImpl whip = scn.GetShadowCard("whip");
		PhysicalCardImpl spear = scn.GetShadowCard("spear");
		scn.ShadowMoveCardToHand(darkness, balrog, whip, spear);

		scn.StartGame();
		scn.SetTwilight(17);
		scn.FreepsSkipCurrentPhaseAction();

		scn.ShadowPlayCard(darkness);
		assertTrue(scn.ShadowActionAvailable("Darkness Grew"));
		scn.ShadowUseCardAction(darkness);
		scn.ShadowChooseCard(whip);
		scn.ShadowUseCardAction(darkness);

		scn.ShadowPlayCard(balrog);

		scn.ShadowUseCardAction(darkness);

		assertEquals(1, scn.GetTwilight());
	}
}
