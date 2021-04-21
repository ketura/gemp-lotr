package com.gempukku.lotro.cards.unofficial.pc.errata.set2;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class FlamingBrandErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("brand", "52_32");
                    put("brand2", "52_32");
                    put("arwen", "1_30");
                    put("boromir", "1_97");
                    put("aragorn", "1_89");

                    put("nazgul", "1_229");
                    put("runner", "1_178");
                }}
        );
    }

    @Test
    public void BrandStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        /**
         * Set: 1E
         * Title: Flaming Brand
         * Side: Free Peoples
         * Culture: Gondor
         * Twilight Cost: 1
         * Type: Possession
         * Subtype: Hand Weapon
         * Errata Game Text: Bearer must be a ranger.  This weapon may be borne in addition to 1 other hand weapon.
         * Skirmish: If bearer is skirmishing a Nazgul, discard this possession to make bearer strength +3 and damage +1.
         */

        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl brand = scn.GetFreepsCard("brand");

        assertFalse(brand.getBlueprint().isUnique());
        assertEquals(1, brand.getBlueprint().getTwilightCost());
        assertEquals(1, brand.getBlueprint().getStrength());
    }

    @Test
    public void CanBeBorneByRangers() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
        PhysicalCardImpl boromir = scn.GetFreepsCard("boromir");
        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl brand = scn.GetFreepsCard("brand");

        scn.FreepsMoveCharToTable(arwen);
        scn.FreepsMoveCharToTable(boromir);
        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(brand);

        scn.StartGame();


        assertTrue(scn.FreepsCardPlayAvailable(brand));

        scn.FreepsPlayCard(brand);

        //There are 3 companions in play, but only 2 rangers, so we should only see 2 options
        assertEquals(2, scn.FreepsGetADParamAsList("cardId").size());
        scn.FreepsChooseCard(aragorn);
        assertTrue(scn.IsAttachedTo(brand, aragorn));

    }

    @Test
    public void CanBeBorneTwice() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl brand = scn.GetFreepsCard("brand");
        PhysicalCardImpl brand2 = scn.GetFreepsCard("brand2");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(brand);
        scn.FreepsMoveCardToHand(brand2);

        scn.StartGame();

        scn.FreepsPlayCard(brand);
        assertTrue(scn.FreepsCardPlayAvailable(brand2));
        scn.FreepsPlayCard(brand2);

        assertEquals(2, scn.GetAttachedCards(aragorn).size());
    }

    @Test
    public void SkirmishAbilityAvailableWhenSkirmishingNazgul() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl brand = scn.GetFreepsCard("brand");
        PhysicalCardImpl runner = scn.GetShadowCard("runner");
        PhysicalCardImpl nazgul = scn.GetShadowCard("nazgul");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(brand);

        scn.ShadowMoveCharToTable(runner);
        scn.ShadowMoveCharToTable(nazgul);

        scn.StartGame();

        assertEquals(8, scn.GetStrength(aragorn));
        scn.FreepsPlayCard(brand);
        assertEquals(9, scn.GetStrength(aragorn));

        scn.SkipToPhase(Phase.ASSIGNMENT);
        scn.SkipCurrentPhaseActions();
        scn.FreepsAssignToMinions(aragorn, runner);
        //skip assigning the nazgul
        scn.SkipCurrentPhaseActions();

        //start goblin skirmish
        scn.FreepsResolveSkirmish(aragorn);

        //goblin shouldn't trigger Flaming Brand's action
        assertEquals(0, scn.FreepsGetAvailableActions().size());
        scn.SkipCurrentPhaseActions();

        //assignment for fierce skirmish
        scn.SkipCurrentPhaseActions();
        scn.FreepsAssignToMinions(aragorn, nazgul);
        scn.FreepsResolveSkirmish(aragorn);

        assertEquals(1, scn.FreepsGetAvailableActions().size());
        assertTrue(scn.FreepsCardActionAvailable(brand));

        assertFalse(scn.HasKeyword(aragorn, Keyword.DAMAGE));
        scn.FreepsUseCardAction(brand);
        assertEquals(11, scn.GetStrength(aragorn));
        assertTrue(scn.HasKeyword(aragorn, Keyword.DAMAGE));

        //Brand is discarded
        assertFalse(scn.IsAttachedTo(brand, aragorn));
    }
}
