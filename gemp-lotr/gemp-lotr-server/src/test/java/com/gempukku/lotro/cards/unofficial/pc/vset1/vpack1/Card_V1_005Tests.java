
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

public class Card_V1_005Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<String, String>()
				{{
					put("onedwarf", "151_5");
					put("gimli", "1_13");
					put("guard", "1_7");
					put("handaxe1", "2_10");
					put("handaxe2", "2_10");
					put("runner", "1_178");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void OneDwarfStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: V1
		* Title: One Dwarf in Moria
		* Side: Free Peoples
		* Culture: dwarven
		* Twilight Cost: 1
		* Type: event
		* Subtype: Maneuver
		* Game Text: Spot a Dwarf bearing 2 possessions to make that Dwarf defender +1 until the regroup phase. 
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl onedwarf = scn.GetFreepsCard("onedwarf");

		assertFalse(onedwarf.getBlueprint().isUnique());
		assertEquals(1, onedwarf.getBlueprint().getTwilightCost());
		assertEquals(CardType.EVENT, onedwarf.getBlueprint().getCardType());
		assertEquals(Culture.DWARVEN, onedwarf.getBlueprint().getCulture());
		assertEquals(Side.FREE_PEOPLE, onedwarf.getBlueprint().getSide());
	}

	@Test
	public void OneDwarfAppliesDefender() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl onedwarf = scn.GetFreepsCard("onedwarf");
		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCardToHand(onedwarf);
		scn.FreepsMoveCharToTable(gimli);
		scn.FreepsMoveCharToTable("guard");
		scn.AttachCardsTo(gimli, scn.GetFreepsCard("handaxe1"), scn.GetFreepsCard("handaxe2"));

		scn.ShadowMoveCharToTable("runner");

		scn.StartGame();
		scn.SkipToPhase(Phase.MANEUVER);
		assertTrue(scn.FreepsActionAvailable("One Dwarf"));

		assertEquals(0, scn.GetKeywordCount(gimli, Keyword.DEFENDER));
		scn.FreepsPlayCard(onedwarf);
		assertEquals(1, scn.GetKeywordCount(gimli, Keyword.DEFENDER));
	}

	@Test
	public void OneDwarfCantBePlayedIfNoDwarvesWithItems() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl onedwarf = scn.GetFreepsCard("onedwarf");
		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCardToHand(onedwarf);
		scn.FreepsMoveCharToTable(gimli);
		scn.FreepsMoveCharToTable("guard");
		scn.AttachCardsTo(gimli, scn.GetFreepsCard("handaxe1"));

		scn.ShadowMoveCharToTable("runner");

		scn.StartGame();
		scn.SkipToPhase(Phase.MANEUVER);
		assertFalse(scn.FreepsActionAvailable("One Dwarf"));

	}
}
