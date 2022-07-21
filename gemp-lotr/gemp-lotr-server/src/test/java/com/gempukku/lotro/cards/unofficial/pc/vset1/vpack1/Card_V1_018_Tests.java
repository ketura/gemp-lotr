
package com.gempukku.lotro.cards.unofficial.pc.vset1.vpack1;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.logic.modifiers.SpecialFlagModifier;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Card_V1_018_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("council", "151_18");
					put("gandalf", "151_14");
					put("elrond", "3_13");
					put("galadriel", "1_45");
					put("aragorn", "1_89");

					put("saruman", "3_69");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void TheWhiteCouncilStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: *The White Council
		* Side: Free Peoples
		* Culture: gandalf
		* Twilight Cost: 1
		* Type: condition
		* Subtype: Support Area
		* Game Text: While you can spot Gandalf and 2 [elven] allies, the first sentence of Saruman's game text does not apply.
		* 	Regroup: Exert 2 [elven] allies to heal Gandalf or a companion with the Gandalf signet (limit 2 per phase).
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl council = scn.GetFreepsCard("council");

		assertTrue(council.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, council.getBlueprint().getSide());
		assertEquals(Culture.GANDALF, council.getBlueprint().getCulture());
		assertEquals(CardType.CONDITION, council.getBlueprint().getCardType());
		//assertEquals(Race.CREATURE, council.getBlueprint().getRace());
		assertTrue(scn.HasKeyword(council, Keyword.SUPPORT_AREA)); // test for keywords as needed
		assertEquals(1, council.getBlueprint().getTwilightCost());
		//assertEquals(, council.getBlueprint().getStrength());
		//assertEquals(, council.getBlueprint().getVitality());
		//assertEquals(, council.getBlueprint().getResistance());
		//assertEquals(Signet., council.getBlueprint().getSignet());
		//assertEquals(, council.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies

	}

	@Test
	public void SpottingGandalfAndTwoAlliesNegatesSarumansFirstSentence() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl council = scn.GetFreepsCard("council");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
		scn.FreepsMoveCardToHand(gandalf, elrond, galadriel);
		scn.FreepsMoveCardToSupportArea(council);
		scn.FreepsMoveCharToTable(aragorn);

		PhysicalCardImpl saruman = scn.GetShadowCard("saruman");
		scn.ShadowMoveCharToTable(saruman);

		scn.StartGame();

		assertFalse(scn.CanBeAssigned(saruman));
		scn.FreepsPlayCard(elrond);
		assertFalse(scn.CanBeAssigned(saruman));
		scn.FreepsPlayCard(galadriel);
		assertFalse(scn.CanBeAssigned(saruman));
		scn.FreepsPlayCard(gandalf);
		assertTrue(scn.CanBeAssigned(saruman));

		scn.SkipToPhase(Phase.ASSIGNMENT);
		scn.PassCurrentPhaseActions();
		scn.FreepsAssignToMinions(gandalf, saruman);
		scn.FreepsResolveSkirmish(gandalf);
	}

	@Test
	public void RegroupActionUnavailableWithoutHealthyElvenAllies() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl council = scn.GetFreepsCard("council");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
		scn.FreepsMoveCardToSupportArea(council);
		scn.FreepsMoveCharToTable(aragorn, gandalf, elrond, galadriel);


		scn.StartGame();

		scn.AddWoundsToChar(galadriel, 2);

		scn.SkipToPhase(Phase.REGROUP);
		assertFalse(scn.FreepsCardActionAvailable(council));
	}

	@Test
	public void RegroupActionExertsAlliesToHeal() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl council = scn.GetFreepsCard("council");
		PhysicalCardImpl gandalf = scn.GetFreepsCard("gandalf");
		PhysicalCardImpl elrond = scn.GetFreepsCard("elrond");
		PhysicalCardImpl galadriel = scn.GetFreepsCard("galadriel");
		PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
		scn.FreepsMoveCardToSupportArea(council);
		scn.FreepsMoveCharToTable(aragorn, gandalf, elrond, galadriel);


		scn.StartGame();

		scn.AddWoundsToChar(aragorn, 3);
		scn.AddWoundsToChar(gandalf, 3);

		scn.SkipToPhase(Phase.REGROUP);
		assertTrue(scn.FreepsCardActionAvailable(council));
		assertEquals(0, scn.GetWoundsOn(galadriel));
		assertEquals(0, scn.GetWoundsOn(elrond));
		assertEquals(3, scn.GetWoundsOn(gandalf));
		assertEquals(3, scn.GetWoundsOn(aragorn));

		scn.FreepsUseCardAction(council);
		//the only 2 elven allies are chosen automatically
		assertEquals(2, scn.GetFreepsCardChoiceCount());
		scn.FreepsChooseCard(aragorn);
		assertEquals(1, scn.GetWoundsOn(galadriel));
		assertEquals(1, scn.GetWoundsOn(elrond));
		assertEquals(3, scn.GetWoundsOn(gandalf));
		assertEquals(2, scn.GetWoundsOn(aragorn));

		scn.ShadowPassCurrentPhaseAction();

		assertTrue(scn.FreepsCardActionAvailable(council));
		scn.FreepsUseCardAction(council);
		assertEquals(2, scn.GetFreepsCardChoiceCount());
		scn.FreepsChooseCard(aragorn);
		assertEquals(2, scn.GetWoundsOn(galadriel));
		assertEquals(2, scn.GetWoundsOn(elrond));
		assertEquals(3, scn.GetWoundsOn(gandalf));
		assertEquals(1, scn.GetWoundsOn(aragorn));

		scn.RemoveWoundsFromChar(galadriel, 1);

		scn.ShadowPassCurrentPhaseAction();

		//limit of 2 means it won't let us even with healthy allies
		assertEquals(1, scn.GetWoundsOn(galadriel));
		assertEquals(2, scn.GetWoundsOn(elrond));
		assertTrue(scn.FreepsCardActionAvailable(council));

		scn.SkipToPhase(Phase.REGROUP);

		assertTrue(scn.FreepsCardActionAvailable(council));
	}
}
