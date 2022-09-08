package com.gempukku.lotro.at.effects;

import com.gempukku.lotro.at.AbstractAtTest;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import static org.junit.Assert.*;

public class HealByDiscardAtTest extends AbstractAtTest {
    @Test
    public void healSuccessful() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));
        final PhysicalCardImpl merryInHand = new PhysicalCardImpl(102, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addWound(merry);
        _game.getGameState().addCardToZone(_game, merryInHand, Zone.HAND);

        skipMulligans();

        playerDecided(P1, "0");

        assertEquals(0, _game.getGameState().getWounds(merry));
        assertEquals(Zone.DISCARD, merryInHand.getZone());
    }

    @Test
    public void cantHealIfNotWounded() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        final PhysicalCardImpl merry = new PhysicalCardImpl(101, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));
        final PhysicalCardImpl merryInHand = new PhysicalCardImpl(102, "1_303", P1, _cardLibrary.getLotroCardBlueprint("1_303"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, merryInHand, Zone.HAND);

        skipMulligans();

        final AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(P1);
        final String[] actionIds = (String[]) awaitingDecision.getDecisionParameters().get("actionId");
        assertEquals(0, actionIds.length);
    }

    @Test
    public void cantHealNonUnique() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        final PhysicalCardImpl lorienElf = new PhysicalCardImpl(101, "1_53", P1, _cardLibrary.getLotroCardBlueprint("1_53"));
        final PhysicalCardImpl lorienElfInHand = new PhysicalCardImpl(102, "1_53", P1, _cardLibrary.getLotroCardBlueprint("1_53"));

        _game.getGameState().addCardToZone(_game, lorienElf, Zone.FREE_CHARACTERS);
        _game.getGameState().addWound(lorienElf);
        _game.getGameState().addCardToZone(_game, lorienElfInHand, Zone.HAND);

        skipMulligans();

        playerDecided(P1, "0");

        assertEquals(1, _game.getGameState().getWounds(lorienElf));
        assertEquals(Zone.FREE_CHARACTERS, lorienElfInHand.getZone());
    }
}
