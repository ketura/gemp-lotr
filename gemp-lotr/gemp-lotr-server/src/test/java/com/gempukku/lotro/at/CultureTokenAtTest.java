package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class CultureTokenAtTest extends AbstractAtTest {
    @Test
    public void removeCultureTokenWithoutOne() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        LotroPhysicalCardImpl sauronsMight = createCard(P2, "19_27");

        _game.getGameState().addCardToZone(_game, sauronsMight, Zone.HAND);

        skipMulligans();

        playerDecided(P1, "");
        assertNull(getCardActionId(P2, "Play Sauron's Might"));
    }
}
