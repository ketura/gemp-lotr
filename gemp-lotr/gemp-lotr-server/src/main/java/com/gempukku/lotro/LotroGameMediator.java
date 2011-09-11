package com.gempukku.lotro;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.ParticipantCommunicationVisitor;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.DefaultLotroGame;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;

public class LotroGameMediator {
    private Map<String, GatheringParticipantCommunicationChannel> _communicationChannels = new HashMap<String, GatheringParticipantCommunicationChannel>();
    private DefaultUserFeedback _userFeedback = new DefaultUserFeedback();
    private DefaultLotroGame _lotroGame;

    public LotroGameMediator(LotroFormat lotroFormat, LotroGameParticipant[] participants, LotroCardBlueprintLibrary library) {
        if (participants.length < 1)
            throw new IllegalArgumentException("Game can't have less than one participant");

        List<String> participantSet = new LinkedList<String>();
        Map<String, LotroDeck> decks = new HashMap<String, LotroDeck>();
        _communicationChannels = new HashMap<String, GatheringParticipantCommunicationChannel>();

        for (LotroGameParticipant participant : participants) {
            String participantId = participant.getPlayerId();
            participantSet.add(participantId);
            decks.put(participantId, participant.getDeck());
        }
        _lotroGame = new DefaultLotroGame(decks, _userFeedback, null, library);
    }

    public synchronized String produceCardInfo(String participantId, int cardId) {
        StringBuilder sb = new StringBuilder();


        PhysicalCard card = _lotroGame.getGameState().findCardById(cardId);
        if (card == null)
            return null;

        sb.append("<b>Affecting card:</b>");
        Collection<Modifier> modifiers = _lotroGame.getModifiersQuerying().getModifiersAffecting(_lotroGame.getGameState(), card);
        for (Modifier modifier : modifiers) {
            PhysicalCard source = modifier.getSource();
            if (source != null)
                sb.append("<br><b>" + source.getBlueprint().getName() + ":</b> " + modifier.getText());
            else
                sb.append("<br><b><i>System</i>:</b> " + modifier.getText());
        }
        if (modifiers.size() == 0)
            sb.append("<br><i>nothing</i>");

        CardType type = card.getBlueprint().getCardType();
        if (type == CardType.COMPANION || type == CardType.ALLY || type == CardType.MINION) {
            sb.append("<br><br><b>Effective stats:</b>");
            sb.append("<br><b>Strength:</b> " + _lotroGame.getModifiersQuerying().getStrength(_lotroGame.getGameState(), card));
            sb.append("<br><b>Vitality:</b> " + _lotroGame.getModifiersQuerying().getVitality(_lotroGame.getGameState(), card));
            if (type == CardType.MINION)
                sb.append("<br><b>Twilight cost:</b> " + _lotroGame.getModifiersQuerying().getTwilightCost(_lotroGame.getGameState(), card));
        }

        return sb.toString();
    }

    public synchronized void startGame() {
        _lotroGame.startGame();
    }

    public synchronized void playerAnswered(String lotroGameParticipant, int decisionId, String answer) {
        AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(lotroGameParticipant);
        if (awaitingDecision != null) {
            if (awaitingDecision.getAwaitingDecisionId() == decisionId) {
                try {
                    _userFeedback.participantDecided(lotroGameParticipant);
                    awaitingDecision.decisionMade(answer);
                    _lotroGame.carryOutPendingActionsUntilDecisionNeeded();
                } catch (DecisionResultInvalidException decisionResultInvalidException) {
                    // Participant provided wrong answer - send a warning message, and ask again for the same decision
//                    _userCommunication.sendWarning(lotroGameParticipant, decisionResultInvalidException.getWarningMessage());
                    _userFeedback.sendAwaitingDecision(lotroGameParticipant, awaitingDecision);
                }
            }
        }
    }

    public synchronized void processCommunicationChannel(String participantId, ParticipantCommunicationVisitor visitor) {
        GatheringParticipantCommunicationChannel communicationChannel = _communicationChannels.get(participantId);
        if (communicationChannel != null)
            for (GameEvent gameEvent : communicationChannel.consumeGameEvents())
                visitor.visitGameEvent(gameEvent);
        String warning = _userFeedback.consumeWarning(participantId);
        if (warning != null)
            visitor.visitWarning(warning);
        AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(participantId);
        if (awaitingDecision != null)
            visitor.visitAwaitingDecision(_lotroGame.getActionStack().getTopmostAction(), awaitingDecision);
    }

    public synchronized void singupUserForGame(String participantId, ParticipantCommunicationVisitor visitor) {
        GatheringParticipantCommunicationChannel participantCommunicationChannel = new GatheringParticipantCommunicationChannel(participantId);
        _communicationChannels.put(participantId, participantCommunicationChannel);

        _lotroGame.addGameStateListener(participantId, participantCommunicationChannel);

        for (GameEvent gameEvent : participantCommunicationChannel.consumeGameEvents())
            visitor.visitGameEvent(gameEvent);

        String warning = _userFeedback.consumeWarning(participantId);
        if (warning != null)
            visitor.visitWarning(warning);
        AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(participantId);
        if (awaitingDecision != null)
            visitor.visitAwaitingDecision(_lotroGame.getActionStack().getTopmostAction(), awaitingDecision);
    }
}
