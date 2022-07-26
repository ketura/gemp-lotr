package com.gempukku.lotro.cards.official.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class Card1_018Tests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("halls", "1_18");
                    put("gimli", "1_13");

                    put("scard1", "1_178");
                    put("scard2", "1_21");
                    put("scard3", "1_203");

                    put("fcard1", "2_75");
                    put("fcard2", "2_51");
                    put("fcard3", "2_96");

                }}
        );
    }

    @Test
    public void HallsStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: V1
         * Title: *A Shadow of the Past
         * Side: Free Peoples
         * Culture: sauron
         * Twilight Cost: 1
         * Type: condition
         * Subtype: Support Area
         * Game Text: While you can spot 4 burdens, each [sauron] Orc is <b>fierce</b>.
         * 	While you can spot 6 burdens, each [Sauron] Orc is damage +1.
         * 	Discard this condition at the start of the regroup phase.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl halls = scn.GetFreepsCard("halls");

        assertFalse(halls.getBlueprint().isUnique());
        assertEquals(Side.FREE_PEOPLE, halls.getBlueprint().getSide());
        assertEquals(Culture.DWARVEN, halls.getBlueprint().getCulture());
        assertEquals(CardType.EVENT, halls.getBlueprint().getCardType());
        //assertEquals(Race.CREATURE, halls.getBlueprint().getRace());
        assertTrue(scn.HasKeyword(halls, Keyword.FELLOWSHIP)); // test for keywords as needed
        assertEquals(1, halls.getBlueprint().getTwilightCost());
        //assertEquals(, halls.getBlueprint().getStrength());
        //assertEquals(, halls.getBlueprint().getVitality());
        //assertEquals(, halls.getBlueprint().getResistance());
        //assertEquals(Signet., halls.getBlueprint().getSignet());
        //assertEquals(, halls.getBlueprint().getSiteNumber()); // Change this to getAllyHomeSiteNumbers for allies

    }

    @Test
    public void HallsExertsToRevealDiscardAndRearrangeFreepsDeck() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl halls = scn.GetFreepsCard("halls");
        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(halls);

        PhysicalCardImpl fcard1 = scn.GetFreepsCard("fcard1");
        PhysicalCardImpl fcard2 = scn.GetFreepsCard("fcard2");
        PhysicalCardImpl fcard3 = scn.GetFreepsCard("fcard3");
        scn.FreepsMoveCardsToTopOfDeck(fcard3, fcard2, fcard1);

        PhysicalCardImpl scard1 = scn.GetShadowCard("scard1");
        PhysicalCardImpl scard2 = scn.GetShadowCard("scard2");
        PhysicalCardImpl scard3 = scn.GetShadowCard("scard3");
        scn.ShadowMoveCardsToTopOfDeck(scard3, scard2, scard1);

        scn.StartGame();

        assertTrue(scn.FreepsCardPlayAvailable(halls));
        assertEquals(0, scn.GetWoundsOn(gimli));

        scn.FreepsPlayCard(halls);

        assertEquals(1, scn.GetWoundsOn(gimli));
        assertTrue(scn.FreepsDecisionAvailable("Choose action to perform"));
        assertEquals(2, scn.FreepsGetMultipleChoices().size());
        scn.FreepsChooseMultipleChoiceOption("your deck");
        List<String> choices = scn.FreepsGetADParamAsList("blueprintId");
        assertTrue(choices.contains(fcard1.getBlueprintId()));
        assertTrue(choices.contains(fcard2.getBlueprintId()));
        assertTrue(choices.contains(fcard3.getBlueprintId()));

        scn.AcknowledgeReveal();
        assertTrue(scn.FreepsDecisionAvailable("Would you like to discard"));
        scn.FreepsChooseYes();
        assertTrue(scn.FreepsDecisionAvailable("Choose card from deck"));

        choices = scn.FreepsGetADParamAsList("blueprintId");
        assertTrue(choices.contains(fcard1.getBlueprintId()));
        assertTrue(choices.contains(fcard2.getBlueprintId()));
        assertFalse(choices.contains(fcard3.getBlueprintId())); // freeps card, shouldn't be able to be discarded

        scn.FreepsChooseCardBPFromSelection(fcard1);
        assertEquals(Zone.DISCARD, fcard1.getZone()); //discarded shadow card
        choices = scn.FreepsGetADParamAsList("blueprintId");
        assertFalse(choices.contains(fcard1.getBlueprintId()));
        assertTrue(choices.contains(fcard2.getBlueprintId()));
        assertTrue(choices.contains(fcard3.getBlueprintId()));

        scn.FreepsChooseCardBPFromSelection(fcard3);
        assertEquals(fcard2.getBlueprintId(), scn.GetFreepsTopOfDeck().getBlueprintId());

    }


    @Test
    public void HallsExertsToRevealDiscardAndRearrangeShadowDeck() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        PhysicalCardImpl halls = scn.GetFreepsCard("halls");
        scn.FreepsMoveCharToTable(gimli);
        scn.FreepsMoveCardToHand(halls);

        PhysicalCardImpl fcard1 = scn.GetFreepsCard("fcard1");
        PhysicalCardImpl fcard2 = scn.GetFreepsCard("fcard2");
        PhysicalCardImpl fcard3 = scn.GetFreepsCard("fcard3");
        scn.FreepsMoveCardsToTopOfDeck(fcard3, fcard2, fcard1);

        PhysicalCardImpl scard1 = scn.GetShadowCard("scard1");
        PhysicalCardImpl scard2 = scn.GetShadowCard("scard2");
        PhysicalCardImpl scard3 = scn.GetShadowCard("scard3");
        scn.ShadowMoveCardsToTopOfDeck(scard3, scard2, scard1);

        scn.StartGame();

        assertTrue(scn.FreepsCardPlayAvailable(halls));
        assertEquals(0, scn.GetWoundsOn(gimli));

        scn.FreepsPlayCard(halls);

        assertEquals(1, scn.GetWoundsOn(gimli));
        assertTrue(scn.FreepsDecisionAvailable("Choose action to perform"));
        assertEquals(2, scn.FreepsGetMultipleChoices().size());
        scn.FreepsChooseMultipleChoiceOption("your opponent's deck");
        List<String> choices = scn.FreepsGetADParamAsList("blueprintId");
        assertTrue(choices.contains(scard1.getBlueprintId()));
        assertTrue(choices.contains(scard2.getBlueprintId()));
        assertTrue(choices.contains(scard3.getBlueprintId()));

        scn.AcknowledgeReveal();
        assertTrue(scn.FreepsDecisionAvailable("Would you like to discard"));
        scn.FreepsChooseYes();
        assertTrue(scn.FreepsDecisionAvailable("Choose card from deck"));

        choices = scn.FreepsGetADParamAsList("blueprintId");
        assertTrue(choices.contains(scard1.getBlueprintId()));
        assertTrue(choices.contains(scard3.getBlueprintId()));
        assertFalse(choices.contains(scard2.getBlueprintId())); // freeps card, shouldn't be able to be discarded

        scn.FreepsChooseCardBPFromSelection(scard1);
        assertEquals(Zone.DISCARD, scard1.getZone()); //discarded shadow card
        choices = scn.FreepsGetADParamAsList("blueprintId");
        assertFalse(choices.contains(scard1.getBlueprintId()));
        assertTrue(choices.contains(scard2.getBlueprintId()));
        assertTrue(choices.contains(scard3.getBlueprintId()));

        scn.FreepsChooseCardBPFromSelection(scard2);
        assertEquals(scard3.getBlueprintId(), scn.GetShadowTopOfDeck().getBlueprintId());

    }



}