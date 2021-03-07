package com.gempukku.lotro.cards.unofficial.pc.errata.set2;

import com.gempukku.lotro.cards.GenericCardTestHelper;
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

public class Gimli_DotMrErrataTests
{
    protected GenericCardTestHelper GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("gimli", "52_121");
                }}
        );
    }

    @Test
    public void GimliHasDamage() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");

        assertTrue(scn.HasKeyword(gimli, Keyword.DAMAGE));
    }


    @Test
    public void GimliRemovesTwilightWhenUnderground() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        scn.FreepsMoveCharToTable(gimli);

        scn.StartGame();

        scn.InsertAdHocModifier(new KeywordModifier(null, Filters.siteNumber(2), Keyword.UNDERGROUND));

        scn.FreepsSkipCurrentPhaseAction();

        // 2 for Frodo/Gimli, 1 for the site, -1 for Gimli's text
        assertEquals(2, scn.GetTwilight());
    }

    @Test
    public void GimliDoesNotRemoveTwilightWhenNotUnderground() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTestHelper scn = GetScenario();

        PhysicalCardImpl gimli = scn.GetFreepsCard("gimli");
        scn.FreepsMoveCharToTable(gimli);

        scn.StartGame();

        scn.FreepsSkipCurrentPhaseAction();

        // 2 for Frodo/Gimli, 1 for the site
        assertEquals(3, scn.GetTwilight());
    }


}
