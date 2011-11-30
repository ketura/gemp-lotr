package com.gempukku.lotro.at;

import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecisionType;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimingAtTest extends AbstractAtTest {
    @Test
    public void playStartingFellowshipWithDiscount() throws DecisionResultInvalidException {
        Map<String, Collection<String>> extraCards = new HashMap<String, Collection<String>>();
        extraCards.put(P1, Arrays.asList("7_88", "6_121"));
        initializeSimplestGame(extraCards);

        // Play first character
        AwaitingDecision firstCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, firstCharacterDecision.getDecisionType());
        assertEquals(2, ((String[]) firstCharacterDecision.getDecisionParameters().get("blueprintId")).length);
        validateContents(new String[]{"7_88", "6_121"}, ((String[]) firstCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(firstCharacterDecision, "7_88"));

        // Play second character with discount
        AwaitingDecision secondCharacterDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.ARBITRARY_CARDS, secondCharacterDecision.getDecisionType());
        assertEquals(1, ((String[]) secondCharacterDecision.getDecisionParameters().get("blueprintId")).length);
        validateContents(new String[]{"6_121"}, ((String[]) secondCharacterDecision.getDecisionParameters().get("blueprintId")));

        playerDecided(P1, getArbitraryCardId(secondCharacterDecision, "6_121"));
    }


}
