package com.gempukku.lotro.at;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TransferAtTest extends AbstractAtTest {
    @Test
    public void transfer() throws DecisionResultInvalidException, CardNotFoundException {
        Map<String, Collection<String>> extraCards = new HashMap<>();
        initializeSimplestGame(extraCards);

        LotroPhysicalCardImpl athelas= new LotroPhysicalCardImpl(100, "1_94", P1, _cardLibrary.getLotroCardBlueprint("1_94"));
        LotroPhysicalCardImpl aragorn= new LotroPhysicalCardImpl(101, "1_89", P1, _cardLibrary.getLotroCardBlueprint("1_89"));
        LotroPhysicalCardImpl boromir= new LotroPhysicalCardImpl(102, "1_96", P1, _cardLibrary.getLotroCardBlueprint("1_96"));

        _game.getGameState().addCardToZone(_game, aragorn, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, boromir, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, athelas, Zone.ATTACHED);
        athelas.attachTo(aragorn);

        skipMulligans();

        final String transferAction = getCardActionId(_userFeedback.getAwaitingDecision(P1), "Transfer");
        playerDecided(P1, transferAction);

        assertEquals(boromir, athelas.getAttachedTo());
    }
}
