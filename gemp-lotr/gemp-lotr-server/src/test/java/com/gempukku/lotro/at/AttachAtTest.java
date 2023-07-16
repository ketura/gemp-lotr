package com.gempukku.lotro.at;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttachAtTest extends AbstractAtTest {
    @Test
    public void extraPossessionClassAttachedTo() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        LotroPhysicalCardImpl aragorn = createCard(P1, "1_89");
        LotroPhysicalCardImpl rangersSword = createCard(P1, "1_112");
        LotroPhysicalCardImpl knifeOfTheGaladhrim = createCard(P1, "9_17");

        _game.getGameState().addCardToZone(_game, aragorn, Zone.FREE_CHARACTERS);
        _game.getGameState().attachCard(_game, rangersSword, aragorn);
        _game.getGameState().addCardToZone(_game, knifeOfTheGaladhrim, Zone.HAND);

        skipMulligans();

        playerDecided(P1, getCardActionId(P1, "Play Knife"));
        assertEquals(Zone.ATTACHED, knifeOfTheGaladhrim.getZone());
    }
}
