package com.gempukku.lotro.cards.unofficial.pc.errata;

import com.gempukku.lotro.cards.GenericCardTest;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoStrangerErrataTest
{
    protected GenericCardTest GetScenario() throws CardNotFoundException, DecisionResultInvalidException {
        return new GenericCardTest(
                new HashMap<String, String>()
                {{
                    put("aragorn", "1_89");
                    put("arwen", "1_30");
                    put("nostranger", "21_10108");
                    put("nostranger2", "21_10108");
                }}
        );
    }


    @Test
    public void ConcealedDoesNothingIfNoTwilight() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");

        scn.FreepsMoveCharToTable(aragorn);
        scn.FreepsMoveCardToHand(nostranger);

        scn.StartGame();

        assertTrue(scn.FreepsCardPlayAvailable(nostranger));
        scn.FreepsPlayCard(nostranger);

        assertTrue(scn.HasKeyword(aragorn, Keyword.CONCEALED));

        assertEquals(0, scn.GetTwilight());
        scn.FreepsSkipCurrentPhaseAction();

        //1 for the ring-bearer, 1 for aragorn, 1 for the site (King's Tent)
        assertEquals(3, scn.GetTwilight());
    }

    @Test
    public void ConcealedRemovesOneIfAvailable() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");

        scn.FreepsMoveCardToHand(aragorn);
        scn.FreepsMoveCardToHand(nostranger);

        scn.StartGame();

        scn.FreepsPlayCard(aragorn);
        scn.FreepsPlayCard(nostranger);

        //4 from playing aragorn
        assertEquals(4, scn.GetTwilight());
        scn.FreepsSkipCurrentPhaseAction();

        //4 from playing aragorn, 1 for the ring-bearer, 1 for aragorn, 1 for the site (King's Tent), -1 for concealed
        assertEquals(6, scn.GetTwilight());
    }

    @Test
    public void TwoConcealedRemovesTwoIfAvailable() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl arwen = scn.GetFreepsCard("arwen");
        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");
        PhysicalCardImpl nostranger2 = scn.GetFreepsCard("nostranger2");

        scn.FreepsMoveCardToHand(aragorn);
        scn.FreepsMoveCharToTable(arwen);
        scn.FreepsMoveCardToHand(nostranger);
        scn.FreepsMoveCardToHand(nostranger2);

        scn.StartGame();

        scn.FreepsPlayCard(aragorn);
        scn.FreepsPlayCard(nostranger);
        scn.FreepsChooseCard(aragorn);
        scn.FreepsPlayCard(nostranger2);


        //4 from playing aragorn
        assertEquals(4, scn.GetTwilight());
        scn.FreepsSkipCurrentPhaseAction();

        //4 from playing aragorn, 1+1+1 for companions, 1 for the site (King's Tent), -2 for concealed
        assertEquals(6, scn.GetTwilight());
    }

    @Test
    public void ConcealedRemovesNothingIfExposed() throws DecisionResultInvalidException, CardNotFoundException {
        //Pre-game setup
        GenericCardTest scn = GetScenario();

        PhysicalCardImpl aragorn = scn.GetFreepsCard("aragorn");
        PhysicalCardImpl nostranger = scn.GetFreepsCard("nostranger");

        scn.FreepsMoveCardToHand(aragorn);
        scn.FreepsMoveCardToHand(nostranger);

        scn.StartGame();

        scn.FreepsPlayCard(aragorn);
        scn.FreepsPlayCard(nostranger);

        scn.InsertAdHocModifier(new KeywordModifier(null, CardType.SITE, Keyword.EXPOSED));

        //4 from playing aragorn
        assertEquals(4, scn.GetTwilight());
        scn.FreepsSkipCurrentPhaseAction();

        //4 from playing aragorn, 1 for the ring-bearer, 1 for aragorn, 1 for the site (King's Tent), 0 for exposed concealed
        assertEquals(7, scn.GetTwilight());
    }
}
