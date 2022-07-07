package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DwarfErrataTests
{

    protected GenericCardTestHelper GetSimpleDeckScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("guard", "51_7");
                    put("farin", "51_11");
                    put("elf", "51_53");
                    put("fror", "52_6");
                    put("gloin", "52_7");
                }}
        );
    }


    @Test
    public void DwarfStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        // Each of these dwarves (and lorien elf) had their strength boosted by 1.

        //Pre-game setup
        GenericCardTestHelper scn = GetSimpleDeckScenario();

        assertEquals(5, scn.GetFreepsCard("guard").getBlueprint().getStrength());
        assertEquals(5, scn.GetFreepsCard("elf").getBlueprint().getStrength());
        assertEquals(6, scn.GetFreepsCard("farin").getBlueprint().getStrength());
        assertEquals(6, scn.GetFreepsCard("fror").getBlueprint().getStrength());
        assertEquals(6, scn.GetFreepsCard("gloin").getBlueprint().getStrength());

    }

}
