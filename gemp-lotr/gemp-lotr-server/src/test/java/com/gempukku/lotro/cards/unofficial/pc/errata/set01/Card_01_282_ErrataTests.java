package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class Card_01_282_ErrataTests
{

	protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
		return new GenericCardTestHelper(
				new HashMap<>()
				{{
					put("legacy", "51_282");
					put("legacy2", "51_282");
					put("orc", "1_280");
					put("orc2", "1_271");

					put("boromir", "1_97");
					put("tale1", "1_24");
					put("tale2", "1_24");
				}},
				GenericCardTestHelper.FellowshipSites,
				GenericCardTestHelper.FOTRFrodo,
				GenericCardTestHelper.FOTRRing
		);
	}

	@Test
	public void TheWeightofaLegacyStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

		/**
		* Set: 1
		* Title: The Weight of a Legacy
		* Side: Free Peoples
		* Culture: Sauron
		* Twilight Cost: 0
		* Type: condition
		* Subtype: 
		* Strength: -1
		* Game Text: To play, exert a [Sauron] Orc. Plays on a [Gondor] companion. Limit 1 per bearer.
		* 	 While you can spot 2 tales, bearer is strength -2.
		*/

		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		var legacy = scn.GetFreepsCard("legacy");

		assertFalse(legacy.getBlueprint().isUnique());
		assertEquals(Side.SHADOW, legacy.getBlueprint().getSide());
		assertEquals(Culture.SAURON, legacy.getBlueprint().getCulture());
		assertEquals(CardType.CONDITION, legacy.getBlueprint().getCardType());
		assertEquals(0, legacy.getBlueprint().getTwilightCost());
		assertEquals(-1, legacy.getBlueprint().getStrength());
	}

	@Test
	public void TheWeightofaLegacyExertsAnOrcToPlay() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		var legacy = scn.GetShadowCard("legacy");
		var orc = scn.GetShadowCard("orc");
		scn.ShadowMoveCardToHand(legacy, orc);

		var boromir = scn.GetFreepsCard("boromir");
		scn.FreepsMoveCharToTable(boromir);

		scn.StartGame();
		scn.SetTwilight(10);
		scn.FreepsPassCurrentPhaseAction();

		assertFalse(scn.ShadowPlayAvailable(legacy));
		scn.ShadowPlayCard(orc);

		assertEquals(Zone.HAND, legacy.getZone());
		assertEquals(0, scn.GetWoundsOn(orc));
		assertEquals(7, scn.GetStrength(boromir));
		assertTrue(scn.ShadowPlayAvailable(legacy));

		scn.ShadowPlayCard(legacy);
		assertEquals(Zone.ATTACHED, legacy.getZone());
		assertEquals(boromir, legacy.getAttachedTo());
		assertEquals(1, scn.GetWoundsOn(orc));
		assertEquals(6, scn.GetStrength(boromir));
	}

	@Test
	public void TheWeightofaLegacyLimit1PerBearer() throws DecisionResultInvalidException, CardNotFoundException {
		//Pre-game setup
		GenericCardTestHelper scn = GetScenario();

		var legacy = scn.GetShadowCard("legacy");

		var boromir = scn.GetFreepsCard("boromir");
		var tale1 = scn.GetFreepsCard("tale1");
		var tale2 = scn.GetFreepsCard("tale2");
		scn.FreepsMoveCardToHand(tale1, tale2);
		scn.FreepsMoveCharToTable(boromir);
		scn.AttachCardsTo(boromir, legacy);

		scn.StartGame();

		assertEquals(6, scn.GetStrength(boromir));
		scn.FreepsPlayCard(tale1);
		assertEquals(6, scn.GetStrength(boromir));
		scn.FreepsPlayCard(tale2);
		assertEquals(4, scn.GetStrength(boromir));
	}
}
