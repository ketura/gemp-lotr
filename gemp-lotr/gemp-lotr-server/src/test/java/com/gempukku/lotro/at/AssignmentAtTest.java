package com.gempukku.lotro.at;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCardImpl;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.AwaitingDecisionType;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

public class AssignmentAtTest extends AbstractAtTest {
    @Test
    public void orcAssassinAssignNotToAlly() throws DecisionResultInvalidException {
        initializeSimplestGame();

        skipMulligans();

        PhysicalCardImpl merry = new PhysicalCardImpl(100, "1_303", P1, _library.getLotroCardBlueprint("1_303"));
        PhysicalCardImpl hobbitPartyGuest = new PhysicalCardImpl(101, "1_297", P1, _library.getLotroCardBlueprint("1_297"));
        PhysicalCardImpl orcAssassin = new PhysicalCardImpl(102, "1_262", P2, _library.getLotroCardBlueprint("1_262"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, hobbitPartyGuest, Zone.SUPPORT);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, orcAssassin, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        playerDecided(P2, "");

        // End maneuvers phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End arhcery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assignment phase
        playerDecided(P1, "");

        AwaitingDecision assignmentActions = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, assignmentActions.getDecisionType());
        playerDecided(P2, "0");

        AwaitingDecision assignCharacter = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, assignCharacter.getDecisionType());
        validateContents(new String[]{String.valueOf(merry.getCardId()), String.valueOf(_game.getGameState().getRingBearer(P1).getCardId())}, (String[]) assignCharacter.getDecisionParameters().get("cardId"));
    }

    @Test
    public void sarumanAssignsToCompainon() throws DecisionResultInvalidException {
        initializeSimplestGame();

        skipMulligans();

        PhysicalCardImpl merry = new PhysicalCardImpl(100, "1_303", P1, _library.getLotroCardBlueprint("1_303"));
        PhysicalCardImpl pippin = new PhysicalCardImpl(101, "1_306", P1, _library.getLotroCardBlueprint("1_306"));
        PhysicalCardImpl saruman = new PhysicalCardImpl(102, "3_69", P2, _library.getLotroCardBlueprint("3_69"));
        PhysicalCardImpl urukHaiRaidingParty = new PhysicalCardImpl(103, "1_158", P2, _library.getLotroCardBlueprint("1_158"));

        _game.getGameState().addCardToZone(_game, merry, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, pippin, Zone.FREE_CHARACTERS);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, saruman, Zone.SHADOW_CHARACTERS);
        _game.getGameState().addCardToZone(_game, urukHaiRaidingParty, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        playerDecided(P2, "");

        // End maneuvers phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End arhcery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assignment phase
        playerDecided(P1, "");

        AwaitingDecision assignmentActions = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, assignmentActions.getDecisionType());
        playerDecided(P2, "0");

        AwaitingDecision chooseMinion = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, chooseMinion.getDecisionType());
        playerDecided(P2, String.valueOf(urukHaiRaidingParty.getCardId()));

        AwaitingDecision chooseCompanion = _userFeedback.getAwaitingDecision(P2);
        assertEquals(AwaitingDecisionType.CARD_SELECTION, chooseCompanion.getDecisionType());
        validateContents(new String[]{String.valueOf(merry.getCardId()), String.valueOf(pippin.getCardId())}, (String[]) chooseCompanion.getDecisionParameters().get("cardId"));
    }
}
