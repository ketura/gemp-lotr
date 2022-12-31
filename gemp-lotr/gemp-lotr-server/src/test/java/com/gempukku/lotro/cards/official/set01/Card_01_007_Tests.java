package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_01_007_Tests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>()
				{{
					put("guard", "1_7");
					put("gimli", "1_13");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void DwarfGuardStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: Dwarf Guard
		* Side: Free Peoples
		* Culture: Dwarven
		* Twilight Cost: 1
		* Type: companion
		* Subtype: Dwarf
		* Strength: 4
		* Vitality: 2
		* Game Text: To play, spot a Dwarf.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl guard = scn.GetFreepsCard("guard");

		assertFalse(guard.getBlueprint().isUnique());
		assertEquals(Side.FREE_PEOPLE, guard.getBlueprint().getSide());
		assertEquals(Culture.DWARVEN, guard.getBlueprint().getCulture());
		assertEquals(CardType.COMPANION, guard.getBlueprint().getCardType());
		assertEquals(Race.DWARF, guard.getBlueprint().getRace());
		assertEquals(1, guard.getBlueprint().getTwilightCost());
		assertEquals(4, guard.getBlueprint().getStrength());
		assertEquals(2, guard.getBlueprint().getVitality());
	}

	@Test
	public void DwarfGuardRequiresDwarf() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		PhysicalCardImpl guard = scn.GetFreepsCard("guard");
		PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
		scn.FreepsMoveCardToHand(guard, gimli);

		scn.StartGame();

		assertFalse(scn.FreepsPlayAvailable(guard));
		scn.FreepsPlayCard(gimli);
		assertTrue(scn.FreepsPlayAvailable(guard));
	}
}
