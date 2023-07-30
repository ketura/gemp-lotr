package com.gempukku.lotro.at;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCardImpl;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.decisions.AwaitingDecision;
import com.gempukku.lotro.decisions.AwaitingDecisionType;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.modifiers.KeywordModifier;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TriggersAtTest extends AbstractAtTest {
    @Test
    public void fpCharWinsSkirmish() throws DecisionResultInvalidException, CardNotFoundException {
        Map<String, Collection<String>> extraCards = new HashMap<>();
        initializeSimplestGame(extraCards);

        LotroPhysicalCardImpl gimli = new LotroPhysicalCardImpl(100, "5_7", P1, _cardLibrary.getLotroCardBlueprint("5_7"));
        LotroPhysicalCardImpl stoutAndStrong = new LotroPhysicalCardImpl(101, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl goblinRunner = new LotroPhysicalCardImpl(102, "1_178", P2, _cardLibrary.getLotroCardBlueprint("1_178"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, gimli, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, stoutAndStrong, Zone.SUPPORT);

        // End fellowship phase
        assertEquals(Phase.FELLOWSHIP, _game.getGameState().getCurrentPhase());
        playerDecided(P1, "");

        _game.getGameState().addCardToZone(_game, goblinRunner, Zone.SHADOW_CHARACTERS);

        // End shadow phase
        assertEquals(Phase.SHADOW, _game.getGameState().getCurrentPhase());
        playerDecided(P2, "");

        // End maneuver phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End archery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End assignment phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assign
        playerDecided(P1, gimli.getCardId() + " " + goblinRunner.getCardId());

        // Start skirmish
        playerDecided(P1, String.valueOf(gimli.getCardId()));

        // End skirmish phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        AwaitingDecision chooseTriggersDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, chooseTriggersDecision.getDecisionType());
        validateContents(new String[]{"" + gimli.getCardId(), "" + stoutAndStrong.getCardId()}, (String[]) chooseTriggersDecision.getDecisionParameters().get("cardId"));
    }

    @Test
    public void musterWorkingWithOtherOptionalStartOfRegroupTrigger() throws DecisionResultInvalidException, CardNotFoundException {
        Map<String, Collection<String>> extraCards = new HashMap<>();
        initializeSimplestGame(extraCards);

        LotroPhysicalCardImpl dervorin = new LotroPhysicalCardImpl(100, "7_88", P1, _cardLibrary.getLotroCardBlueprint("7_88"));
        LotroPhysicalCardImpl boromir = new LotroPhysicalCardImpl(101, "1_96", P1, _cardLibrary.getLotroCardBlueprint("1_96"));
        LotroPhysicalCardImpl cardInHand1 = new LotroPhysicalCardImpl(102, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand2 = new LotroPhysicalCardImpl(103, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand3 = new LotroPhysicalCardImpl(104, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand4 = new LotroPhysicalCardImpl(105, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, dervorin, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, boromir, Zone.FREE_CHARACTERS);
        _game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                new KeywordModifier(dervorin, dervorin, Keyword.MUSTER));

        _game.getGameState().addCardToZone(_game, cardInHand1, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand2, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand3, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand4, Zone.HAND);

        // End fellowship phase
        playerDecided(P1, "");

        // End shadow phase
        playerDecided(P2, "");

        final AwaitingDecision optionalStartOfRegroupDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalStartOfRegroupDecision.getDecisionType());
        validateContents(new String[]{"" + dervorin.getCardId(), "" + dervorin.getCardId()}, (String[]) optionalStartOfRegroupDecision.getDecisionParameters().get("cardId"));

        playerDecided(P1, getCardActionId(optionalStartOfRegroupDecision, "Optional "));

        final AwaitingDecision optionalSecondStartOfRegroupDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalSecondStartOfRegroupDecision.getDecisionType());
        validateContents(new String[]{"" + dervorin.getCardId()}, (String[]) optionalSecondStartOfRegroupDecision.getDecisionParameters().get("cardId"));
    }

    @Test
    public void userOfMusterAllowsUseOfOtherOptionalStartOfRegroupTrigger() throws DecisionResultInvalidException, CardNotFoundException {
        Map<String, Collection<String>> extraCards = new HashMap<>();
        initializeSimplestGame(extraCards);

        LotroPhysicalCardImpl dervorin = new LotroPhysicalCardImpl(100, "7_88", P1, _cardLibrary.getLotroCardBlueprint("7_88"));
        LotroPhysicalCardImpl boromir = new LotroPhysicalCardImpl(101, "1_96", P1, _cardLibrary.getLotroCardBlueprint("1_96"));
        LotroPhysicalCardImpl cardInHand1 = new LotroPhysicalCardImpl(102, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand2 = new LotroPhysicalCardImpl(103, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand3 = new LotroPhysicalCardImpl(104, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand4 = new LotroPhysicalCardImpl(105, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand5 = new LotroPhysicalCardImpl(106, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, dervorin, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, boromir, Zone.FREE_CHARACTERS);
        _game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                new KeywordModifier(dervorin, dervorin, Keyword.MUSTER));

        _game.getGameState().addCardToZone(_game, cardInHand1, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand2, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand3, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand4, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand5, Zone.HAND);

        // End fellowship phase
        playerDecided(P1, "");

        // End shadow phase
        playerDecided(P2, "");

        final AwaitingDecision optionalStartOfRegroupDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalStartOfRegroupDecision.getDecisionType());
        validateContents(new String[]{"" + dervorin.getCardId()}, (String[]) optionalStartOfRegroupDecision.getDecisionParameters().get("cardId"));

        playerDecided(P1, getCardActionId(optionalStartOfRegroupDecision, "Use "));

        playerDecided(P1, String.valueOf(cardInHand1.getCardId()));

        final AwaitingDecision optionalSecondStartOfRegroupDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalSecondStartOfRegroupDecision.getDecisionType());
        validateContents(new String[]{"" + dervorin.getCardId()}, (String[]) optionalSecondStartOfRegroupDecision.getDecisionParameters().get("cardId"));
        assertTrue(((String[]) optionalSecondStartOfRegroupDecision.getDecisionParameters().get("actionText"))[0].startsWith("Optional "));
    }

    @Test
    public void userOfMusterDisablesUseOfOtherOptionalStartOfRegroupTrigger() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        LotroPhysicalCardImpl dervorin = new LotroPhysicalCardImpl(100, "7_88", P1, _cardLibrary.getLotroCardBlueprint("7_88"));
        LotroPhysicalCardImpl boromir = new LotroPhysicalCardImpl(101, "1_96", P1, _cardLibrary.getLotroCardBlueprint("1_96"));
        LotroPhysicalCardImpl cardInHand1 = new LotroPhysicalCardImpl(102, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand2 = new LotroPhysicalCardImpl(103, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand3 = new LotroPhysicalCardImpl(104, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));
        LotroPhysicalCardImpl cardInHand4 = new LotroPhysicalCardImpl(105, "4_57", P1, _cardLibrary.getLotroCardBlueprint("4_57"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, dervorin, Zone.FREE_CHARACTERS);
        _game.getGameState().addCardToZone(_game, boromir, Zone.FREE_CHARACTERS);
        _game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                new KeywordModifier(dervorin, dervorin, Keyword.MUSTER));

        _game.getGameState().addCardToZone(_game, cardInHand1, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand2, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand3, Zone.HAND);
        _game.getGameState().addCardToZone(_game, cardInHand4, Zone.HAND);

        // End fellowship phase
        playerDecided(P1, "");

        // End shadow phase
        playerDecided(P2, "");

        final AwaitingDecision optionalStartOfRegroupDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, optionalStartOfRegroupDecision.getDecisionType());
        validateContents(new String[]{"" + dervorin.getCardId(), "" + dervorin.getCardId()}, (String[]) optionalStartOfRegroupDecision.getDecisionParameters().get("cardId"));

        playerDecided(P1, getCardActionId(optionalStartOfRegroupDecision, "Use "));
        playerDecided(P1, String.valueOf(cardInHand1.getCardId()));

        final AwaitingDecision regroupPhaseActionDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, regroupPhaseActionDecision.getDecisionType());
        validateContents(new String[]{}, (String[]) regroupPhaseActionDecision.getDecisionParameters().get("cardId"));

        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
    }

    @Test
    public void musterForShadowSideTriggersCorrectly() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        LotroPhysicalCardImpl musterWitchKing = new LotroPhysicalCardImpl(100, "11_226", P2, _cardLibrary.getLotroCardBlueprint("11_226"));
        LotroPhysicalCardImpl musterWitchKing2 = new LotroPhysicalCardImpl(101, "11_226", P2, _cardLibrary.getLotroCardBlueprint("11_226"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, musterWitchKing, Zone.SHADOW_CHARACTERS);
        _game.getGameState().addCardToZone(_game, musterWitchKing2, Zone.HAND);

        // End fellowship phase
        playerDecided(P1, "");

        // End shadow phase
        playerDecided(P2, "");

        // End maneuver phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End archery phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End assignment phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Assign
        playerDecided(P1, "");
        playerDecided(P2, "");

        // End fierce assignment phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Fierce assign
        playerDecided(P1, "");
        playerDecided(P2, "");

        // Start regroup
        assertEquals(Phase.REGROUP, _game.getGameState().getCurrentPhase());
    }

    @Test
    public void replaceSiteNotPossibleWithMountDoom() throws DecisionResultInvalidException, CardNotFoundException {
        initializeSimplestGame();

        LotroPhysicalCardImpl gandalf = new LotroPhysicalCardImpl(100, "1_72", P1, _cardLibrary.getLotroCardBlueprint("1_72"));
        LotroPhysicalCardImpl traveledLeader = new LotroPhysicalCardImpl(101, "12_34", P1, _cardLibrary.getLotroCardBlueprint("12_34"));
        LotroPhysicalCardImpl mountDoom = new LotroPhysicalCardImpl(102, "15_193", P2, _cardLibrary.getLotroCardBlueprint("15_193"));

        skipMulligans();

        _game.getGameState().addCardToZone(_game, traveledLeader, Zone.HAND);
        _game.getGameState().addCardToZone(_game, gandalf, Zone.FREE_CHARACTERS);

        // End fellowship phase
        playerDecided(P1, "");

        _game.getGameState().removeCardsFromZone(P2, Collections.singleton(_game.getGameState().getSite(2)));
        mountDoom.setSiteNumber(2);
        _game.getGameState().addCardToZone(_game, mountDoom, Zone.ADVENTURE_PATH);

        // End shadow phase
        playerDecided(P2, "");

        // End regroup phase
        playerDecided(P1, "");
        playerDecided(P2, "");

        playerDecided(P1, "1");

        // Reconcile
        playerDecided(P1, "");

        playerDecided(P2, "");
        playerDecided(P1, "");
        playerDecided(P2, "");
        playerDecided(P1, "");

        // Reconcile
        playerDecided(P1, "");

        playerDecided(P2, "1");

        playerDecided(P1, "");
        playerDecided(P2, "");

        final AwaitingDecision regroupActionDecision = _userFeedback.getAwaitingDecision(P1);
        assertEquals(AwaitingDecisionType.CARD_ACTION_CHOICE, regroupActionDecision.getDecisionType());
        validateContents(new String[]{"" + traveledLeader.getCardId()}, (String[]) regroupActionDecision.getDecisionParameters().get("cardId"));

        playerDecided(P1, "0");

        LotroPhysicalCard siteOne = _game.getGameState().getSite(1);

        playerDecided(P1, "" + siteOne.getCardId());

        assertEquals(siteOne, _game.getGameState().getSite(1));
    }
}
