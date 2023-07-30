package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.decisions.AwaitingDecision;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FollowerAtTest extends AbstractAtTest {
    @Test
    public void aidFollower() throws DecisionResultInvalidException, CardNotFoundException {
        Map<String, Collection<String>> extraCards = new HashMap<>();
        initializeSimplestGame(extraCards);

        LotroPhysicalCardImpl pallando = new LotroPhysicalCardImpl(100, "13_37", P1, _cardLibrary.getLotroCardBlueprint("13_37"));
        LotroPhysicalCardImpl goblinWarrior = new LotroPhysicalCardImpl(101, "1_185", P2, _cardLibrary.getLotroCardBlueprint("1_185"));

        _game.getGameState().addCardToZone(_game, pallando, Zone.HAND);
        _game.getGameState().addCardToZone(_game, goblinWarrior, Zone.HAND);

        skipMulligans();

        // Play Pallando
        playerDecided(P1, "0");
        // Pass
        playerDecided(P1, "");

        // Play Goblin Warrior
        playerDecided(P2, "0");
        // Pass on the Goblin Warrior ability
        playerDecided(P2, "");
        // Pass on King's Tent
        playerDecided(P2, "");

        final AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(P1);
        assertTrue(((String[]) awaitingDecision.getDecisionParameters().get("actionText"))[0].endsWith("Aid"));
        assertEquals(4, _game.getModifiersQuerying().getStrength(_game, _game.getGameState().getRingBearer(P1)));

        // Aid with Pallando
        playerDecided(P1, "0");

        assertEquals(2, _game.getGameState().getTwilightPool());

        assertEquals(Zone.ATTACHED, pallando.getZone());
        assertEquals(5, _game.getModifiersQuerying().getStrength(_game, _game.getGameState().getRingBearer(P1)));
    }
}
