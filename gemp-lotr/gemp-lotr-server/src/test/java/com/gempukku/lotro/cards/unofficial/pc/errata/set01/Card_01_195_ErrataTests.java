package com.gempukku.lotro.cards.unofficial.pc.errata.set01;

import com.gempukku.lotro.cards.GenericCardTestHelper;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Assert;
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
    public void RelicsStatsAndKeywordsAreCorrect() throws DecisionResultInvalidException, CardNotFoundException {

        //Pre-game setup
        GenericCardTestHelper scn = GetSimpleDeckScenario();
        PhysicalCardImpl relics = scn.GetFreepsCard("relics");

        assertTrue(relics.getBlueprint().isUnique());
        assertEquals(CardType.CONDITION, relics.getBlueprint().getCardType());
        assertEquals(Culture.MORIA, relics.getBlueprint().getCulture());
        Assert.assertTrue(scn.HasKeyword(relics, Keyword.SUPPORT_AREA));
    }

}
