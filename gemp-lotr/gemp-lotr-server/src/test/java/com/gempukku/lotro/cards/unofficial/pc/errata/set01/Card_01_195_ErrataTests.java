package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class Card_01_195_ErrataTests
{

    protected GenericCardTestHelper GetSimpleDeckScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTestHelper(
                new HashMap<String, String>()
                {{
                    put("relics", "51_195");
                }}
        );
    }


    @Test
    public void DwarfStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        // Each of these dwarves (and lorien elf) had their strength boosted by 1.

        //Pre-game setup
        GenericCardTestHelper scn = GetSimpleDeckScenario();

        assertTrue( scn.GetFreepsCard("relics").getBlueprint().isUnique());
    }

}
