package com.gempukku.lotro.cards.unofficial.pc.errata.set10;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Card_10_068_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>()
				{{
					put("enquea", "80_68");
					// put other cards in here as needed for the test case
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void UlaireEnqueaStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 10
		* Title: Ulaire Enquea, Thrall of The One
		* Unique: True
		* Side: SHADOW
		* Culture: Wraith
		* Twilight Cost: 6
		* Type: minion
		* Subtype: Nazgul
		* Strength: 11
		* Vitality: 4
		* Site Number: 3
		* Game Text: <b>Enduring.</b> <br>Shadow cards cannot exert Ulaire Enquea during a skirmish phase. <br><b>Skirmish:</b> If Ulaire Enquea is skirmishing, remove (1) and heal him to add a burden.
		*/

		//Pre-game setup
		var scn = GetScenario();

		var enquea = scn.GetFreepsCard("enquea");

		assertTrue(enquea.getBlueprint().isUnique());
		assertEquals(Side.SHADOW, enquea.getBlueprint().getSide());
		assertEquals(Culture.WRAITH, enquea.getBlueprint().getCulture());
		assertEquals(CardType.MINION, enquea.getBlueprint().getCardType());
		assertEquals(Race.NAZGUL, enquea.getBlueprint().getRace());
		assertTrue(scn.HasKeyword(enquea, Keyword.ENDURING));
		assertEquals(6, enquea.getBlueprint().getTwilightCost());
		assertEquals(11, enquea.getBlueprint().getStrength());
		assertEquals(4, enquea.getBlueprint().getVitality());
		assertEquals(3, enquea.getBlueprint().getSiteNumber());
	}

	//@Test
	public void UlaireEnqueaTest1() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		var scn = GetScenario();

		var card = scn.GetFreepsCard("card");
		scn.FreepsMoveCardToHand(card);

		scn.StartGame();
		scn.FreepsPlayCard(card);

		assertEquals(6, scn.GetTwilight());
	}
}
