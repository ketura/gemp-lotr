
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

public class Card_V1_064_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("greenleaf", "1_50");
					put("savage", "1_151");
					put("lord", "4_219");
				}},
				new HashMap<String, String>() {{
					put("site1", "1_319");
					put("site2", "1_327");
					put("site3", "1_337");
					put("site4", "1_343");
					put("site5", "1_349");
					put("site6", "1_350");
					put("site7", "1_353");
					put("site8", "1_356");
					put("site9", "151_64");
				}},
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void AmonHenRuinsStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: Amon Hen Ruins
		* Side: Free Peoples
		* Culture: 
		* Twilight Cost: 9
		* Type: site
		* Subtype: 
		* Site Number: 9
		* Game Text: Forest. Exhausted minions cannot take wounds.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl site9 = scn.GetFreepsSite(9);

		assertFalse(site9.getBlueprint().isUnique());
		//assertEquals(Side.FREE_PEOPLE, site9.getBlueprint().getSide());
		//assertEquals(Culture., card.getBlueprint().getCulture());
		assertEquals(CardType.SITE, site9.getBlueprint().getCardType());
		//assertEquals(Race.CREATURE, card.getBlueprint().getRace());
		assertTrue(scn.HasKeyword(site9, Keyword.FOREST)); // test for keywords as needed
		assertEquals(9, site9.getBlueprint().getTwilightCost());
		//assertEquals(, card.getBlueprint().getStrength());
		//assertEquals(, card.getBlueprint().getVitality());
		//assertEquals(, card.getBlueprint().getResistance());
		//assertEquals(Signet., card.getBlueprint().getSignet()); 
		assertEquals(9, site9.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void WoundedMinionsTakeNoWoundsAndWoundedCompsCannotExertFromFPCards() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl greenleaf = scn.GetFreepsCard("greenleaf");
		scn.FreepsMoveCharToTable(greenleaf);

		PhysicalCardImpl savage = scn.GetShadowCard("savage");
		PhysicalCardImpl lord = scn.GetShadowCard("lord");
		scn.ShadowMoveCardToHand(savage, lord);

		//Max out the move limit so we don't have to juggle play back and forth
		scn.ApplyAdHocModifier(new MoveLimitModifier(null, 10));

		scn.StartGame();

		// 1 -> 3
		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();

		// 3 -> 4
		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();

		// 4 -> 5
		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();

		// 5 -> 6
		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();

		// 6 -> 7
		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();

		// 7 -> 8
		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();

		// 8 -> 9
		scn.SkipToPhase(Phase.REGROUP);
		scn.PassCurrentPhaseActions();
		scn.ShadowDeclineReconciliation();
		scn.FreepsChooseToMove();

		scn.ShadowMoveCharToTable(savage, lord);

		scn.SkipToPhase(Phase.ARCHERY);

		assertEquals(0, scn.GetWoundsOn(greenleaf));
		assertEquals(0, scn.GetWoundsOn(savage));
		assertTrue(scn.FreepsCardActionAvailable(greenleaf));
		scn.FreepsUseCardAction(greenleaf);
		scn.FreepsChooseCard(savage);

		assertEquals(1, scn.GetWoundsOn(greenleaf));
		// The Free Peoples player may not exert wounded companions, but Shadow cards may.
		scn.ShadowUseCardAction(lord);

		assertFalse(scn.FreepsCardActionAvailable(greenleaf));
		//pass remaining archery actions
		scn.PassCurrentPhaseActions();
		//pass assignment actions
		scn.PassCurrentPhaseActions();

		assertEquals(2, scn.GetWoundsOn(greenleaf));
		assertEquals(1, scn.GetWoundsOn(savage));

		scn.FreepsAssignToMinions(greenleaf, savage);
		scn.ShadowPassCurrentPhaseAction();
		scn.FreepsResolveSkirmish(greenleaf);
		scn.PassCurrentPhaseActions();

		//As a wounded minion, the Savage was barred from taking a wound
		assertEquals(2, scn.GetWoundsOn(greenleaf));
		assertEquals(1, scn.GetWoundsOn(savage));
	}
}
