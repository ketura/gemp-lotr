package com.gempukku.lotro.cards.unofficial.pc.errata;

import com.gempukku.lotro.cards.GenericCardTest;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Gimli_DotMrErrataTest
{
    protected GenericCardTest GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("gimli", "21_20121");
                }}
        );
    }

    @Test
    public void GimliHasDamage() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");

        assertTrue(scn.HasKeyword(gimli, Keyword.DAMAGE));
    }


    @Test
    public void GimliHasConcealedOnlyWhileUnderground() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        scn.FreepsMoveCharToTable(gimli);

        scn.StartGame();

        assertFalse(scn.HasKeyword(gimli, Keyword.CONCEALED));

        scn.InsertAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.UNDERGROUND));

        scn.FreepsSkipCurrentPhaseAction();

        assertTrue(scn.HasKeyword(gimli, Keyword.CONCEALED));
    }

    @Test
    public void GimliAbilityTriggersMovingFromAndTo() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetScenario();

        PhysicalCardImpl frodo = scn.GetRingBearer();
        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");

        scn.FreepsMoveCharToTable(gimli);

        scn.StartGame();

        scn.InsertAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.UNDERGROUND));

        scn.FreepsSkipCurrentPhaseAction();

        assertTrue(scn.FreepsActionAvailable("Optional"));
        scn.FreepsActivateOptionalTrigger();
        assertEquals(1, scn.GetWoundsOn(gimli));
        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));

        scn.SkipToPhase(Phase.REGROUP);
        scn.SkipCurrentPhaseActions();
        scn.ShadowSkipCurrentPhaseAction();
        assertFalse(scn.HasKeyword(frodo, Keyword.CONCEALED));
        scn.FreepsChooseToMove();

        assertTrue(scn.FreepsActionAvailable("Optional"));
        scn.FreepsActivateOptionalTrigger();
        assertEquals(2, scn.GetWoundsOn(gimli));
        assertTrue(scn.HasKeyword(frodo, Keyword.CONCEALED));
    }

}
