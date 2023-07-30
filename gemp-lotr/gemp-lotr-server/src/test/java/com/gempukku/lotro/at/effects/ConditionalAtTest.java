package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.decisions.AwaitingDecisionType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConditionalAtTest extends AbstractAtTest {
    @Test
    public void savageryToMatchTheirNumbersOffersAChoice() throws Exception {
        initializeSimplestGame();

        skipMulligans();

        for (int i = 0; i < 4; i++) {
            final LotroPhysicalCardImpl pippin = createCard(P1, "1_306");
            _game.getGameState().addCardToZone(_game, pippin, Zone.FREE_CHARACTERS);
        }

        LotroPhysicalCardImpl troopOfUrukHai = createCard(P2, "1_143");
        _game.getGameState().addCardToZone(_game, troopOfUrukHai, Zone.SHADOW_CHARACTERS);

        LotroPhysicalCardImpl savageryToMatchTheirNumbers = createCard(P2, "1_139");
        _game.getGameState().addCardToZone(_game, savageryToMatchTheirNumbers, Zone.HAND);

        // End Fellowship
        playerDecided(P1, "");

        // End Shadow
        playerDecided(P2, "");

        // End Maneuvers
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End Archery
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End assignment phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        playerDecided(P1, _game.getGameState().getRingBearer(P1).getCardId() + " " + troopOfUrukHai.getCardId());

        playerDecided(P1, ""+_game.getGameState().getRingBearer(P1).getCardId());

        playerDecided(P1, "");
        playerDecided(P2, getCardActionId(P2, "Play Savagery"));

        assertEquals(AwaitingDecisionType.MULTIPLE_CHOICE, _userFeedback.getAwaitingDecision(P2).getDecisionType());
    }
}
