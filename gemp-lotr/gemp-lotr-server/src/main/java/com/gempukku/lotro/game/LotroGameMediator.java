package com.gempukku.lotro.game;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.formats.LotroFormat;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.DefaultLotroGame;
import com.gempukku.lotro.logic.timing.GameResultListener;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LotroGameMediator {
    private Map<String, GatheringParticipantCommunicationChannel> _communicationChannels = new HashMap<String, GatheringParticipantCommunicationChannel>();
    private DefaultUserFeedback _userFeedback = new DefaultUserFeedback();
    private DefaultLotroGame _lotroGame;
    private Map<String, Integer> _playerClocks = new HashMap<String, Integer>();
    private Map<String, Long> _decisionQuerySentTimes = new HashMap<String, Long>();
    private Set<String> _playersPlaying = new HashSet<String>();

    private final int _maxSecondsForGamePerPlayer = 60 * 30; // 30 minutes
    private final int _channelInactivityTimeoutPeriod = 1000 * 60 * 5; // 5 minutes
    private final int _playerDecisionTimeoutPeriod = 1000 * 60 * 10; // 10 minutes

    private ReentrantReadWriteLock _lock = new ReentrantReadWriteLock(true);
    private ReentrantReadWriteLock.ReadLock _readLock = _lock.readLock();
    private ReentrantReadWriteLock.WriteLock _writeLock = _lock.writeLock();

    public LotroGameMediator(LotroFormat lotroFormat, LotroGameParticipant[] participants, LotroCardBlueprintLibrary library, GameResultListener gameResultListener) {
        if (participants.length < 1)
            throw new IllegalArgumentException("Game can't have less than one participant");

        Map<String, LotroDeck> decks = new HashMap<String, LotroDeck>();
        _communicationChannels = new HashMap<String, GatheringParticipantCommunicationChannel>();

        for (LotroGameParticipant participant : participants) {
            String participantId = participant.getPlayerId();
            decks.put(participantId, participant.getDeck());
            _playerClocks.put(participantId, 0);
            _playersPlaying.add(participantId);
        }
        _lotroGame = new DefaultLotroGame(decks, _userFeedback, gameResultListener, library);
    }

    public Set<String> getPlayersPlaying() {
        return Collections.unmodifiableSet(_playersPlaying);
    }

    public String getGameStatus() {
        if (_lotroGame.getWinnerPlayerId() != null)
            return "Finished";
        if (_lotroGame.getGameState().getCurrentPhase() == Phase.GAME_SETUP)
            return "Preparation";
        return "Playing";
    }

    public String produceCardInfo(String participantId, int cardId) {
        _readLock.lock();
        try {
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
        } finally {
            _readLock.unlock();
        }
    }

    public void startGame() {
        _writeLock.lock();
        try {
            _lotroGame.startGame();
            startClocksForUsersPendingDecision();
        } finally {
            _writeLock.unlock();
        }
    }

    public void cleanup() {
        _writeLock.lock();
        try {
            long currentTime = System.currentTimeMillis();
            Map<String, GatheringParticipantCommunicationChannel> channelsCopy = new HashMap<String, GatheringParticipantCommunicationChannel>(_communicationChannels);
            for (Map.Entry<String, GatheringParticipantCommunicationChannel> playerChannels : channelsCopy.entrySet()) {
                String playerId = playerChannels.getKey();
                // Channel is stale (user no longer connected to game, to save memory, we remove the channel
                // User can always reconnect and establish a new channel
                GatheringParticipantCommunicationChannel channel = playerChannels.getValue();
                if (currentTime > channel.getLastConsumed().getTime() + _channelInactivityTimeoutPeriod) {
                    _lotroGame.removeGameStateListener(playerId, channel);
                    _communicationChannels.remove(playerId);
                }
            }

            if (_lotroGame.getGameState() != null && _lotroGame.getWinnerPlayerId() == null) {
                for (Map.Entry<String, Long> playerDecision : _decisionQuerySentTimes.entrySet()) {
                    String playerId = playerDecision.getKey();
                    long decisionSent = playerDecision.getValue();
                    if (currentTime > decisionSent + _playerDecisionTimeoutPeriod)
                        _lotroGame.playerLost(playerId, "Player decision timed-out");
                }

                for (Map.Entry<String, Integer> playerClock : _playerClocks.entrySet()) {
                    String player = playerClock.getKey();
                    if (_maxSecondsForGamePerPlayer - playerClock.getValue() - getCurrentUserPendingTime(player) < 0)
                        _lotroGame.playerLost(player, "Player run out of time");
                }
            }
        } finally {
            _writeLock.unlock();
        }
    }

    public void concede(String playerId) {
        _writeLock.lock();
        try {
            if (_lotroGame.getWinnerPlayerId() == null)
                _lotroGame.playerLost(playerId, "Concession");
        } finally {
            _writeLock.unlock();
        }
    }

    public void playerAnswered(String lotroGameParticipant, int decisionId, String answer) {
        _writeLock.lock();
        try {
            AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(lotroGameParticipant);
            if (awaitingDecision != null) {
                if (awaitingDecision.getAwaitingDecisionId() == decisionId) {
                    try {
                        _userFeedback.participantDecided(lotroGameParticipant);
                        awaitingDecision.decisionMade(answer);

                        // Decision successfully made, add the time to user clock
                        addTimeSpentOnDecisionToUserClock(lotroGameParticipant);

                        _lotroGame.carryOutPendingActionsUntilDecisionNeeded();
                        startClocksForUsersPendingDecision();

                    } catch (DecisionResultInvalidException decisionResultInvalidException) {
                        // Participant provided wrong answer - send a warning message, and ask again for the same decision
                        _userFeedback.sendWarning(lotroGameParticipant, decisionResultInvalidException.getWarningMessage());
                        _userFeedback.sendAwaitingDecision(lotroGameParticipant, awaitingDecision);
                    }
                }
            }
        } finally {
            _writeLock.unlock();
        }
    }

    public void processCommunicationChannel(String participantId, ParticipantCommunicationVisitor visitor) {
        _readLock.lock();
        try {
            GatheringParticipantCommunicationChannel communicationChannel = _communicationChannels.get(participantId);
            if (communicationChannel != null) {
                for (GameEvent gameEvent : communicationChannel.consumeGameEvents())
                    visitor.visitGameEvent(gameEvent);

                String warning = _userFeedback.consumeWarning(participantId);
                if (warning != null)
                    visitor.visitGameEvent(new GameEvent(GameEvent.Type.WARNING).message(warning));

                AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(participantId);
                if (awaitingDecision != null)
                    visitor.visitAwaitingDecision(awaitingDecision);

                Map<String, Integer> secondsLeft = new HashMap<String, Integer>();
                for (Map.Entry<String, Integer> playerClock : _playerClocks.entrySet()) {
                    String player = playerClock.getKey();
                    secondsLeft.put(player, _maxSecondsForGamePerPlayer - playerClock.getValue() - getCurrentUserPendingTime(player));
                }
                visitor.visitClock(secondsLeft);

                GameState gameState = _lotroGame.getGameState();
                if (gameState != null) {
                    Skirmish skirmish = gameState.getSkirmish();
                    if (skirmish != null) {
                        int fpStrength = 0;
                        PhysicalCard fpChar = skirmish.getFellowshipCharacter();
                        if (fpChar != null)
                            fpStrength = _lotroGame.getModifiersQuerying().getStrength(gameState, fpChar);

                        int minionStrength = 0;
                        for (PhysicalCard minion : skirmish.getShadowCharacters())
                            minionStrength += _lotroGame.getModifiersQuerying().getStrength(gameState, minion);

                        visitor.visitSkirmishStats(fpStrength, minionStrength);
                    }
                }
            } else {
                visitor.visitGameEvent(new GameEvent(GameEvent.Type.WARNING).message("Your browser was inactive for too long, please refresh your browser window to continue playing"));
            }
        } finally {
            _readLock.unlock();
        }
    }

    public void singupUserForGame(String participantId, ParticipantCommunicationVisitor visitor) {
        _readLock.lock();
        try {
            GatheringParticipantCommunicationChannel participantCommunicationChannel = new GatheringParticipantCommunicationChannel(participantId);
            _communicationChannels.put(participantId, participantCommunicationChannel);

            _lotroGame.addGameStateListener(participantId, participantCommunicationChannel);

            for (GameEvent gameEvent : participantCommunicationChannel.consumeGameEvents())
                visitor.visitGameEvent(gameEvent);

            AwaitingDecision awaitingDecision = _userFeedback.getAwaitingDecision(participantId);
            if (awaitingDecision != null)
                visitor.visitAwaitingDecision(awaitingDecision);

            Map<String, Integer> secondsLeft = new HashMap<String, Integer>();
            for (Map.Entry<String, Integer> playerClock : _playerClocks.entrySet()) {
                String player = playerClock.getKey();
                secondsLeft.put(player, _maxSecondsForGamePerPlayer - playerClock.getValue() - getCurrentUserPendingTime(player));
            }
            visitor.visitClock(secondsLeft);
        } finally {
            _readLock.unlock();
        }
    }

    private void startClocksForUsersPendingDecision() {
        long currentTime = System.currentTimeMillis();
        Set<String> users = _userFeedback.getUsersPendingDecision();
        for (String user : users)
            _decisionQuerySentTimes.put(user, currentTime);
    }

    private void addTimeSpentOnDecisionToUserClock(String participantId) {
        long queryTime = _decisionQuerySentTimes.remove(participantId);
        long currentTime = System.currentTimeMillis();
        long diffSec = (currentTime - queryTime) / 1000;
        _playerClocks.put(participantId, _playerClocks.get(participantId) + (int) diffSec);
    }

    private int getCurrentUserPendingTime(String participantId) {
        if (!_decisionQuerySentTimes.containsKey(participantId))
            return 0;
        long queryTime = _decisionQuerySentTimes.get(participantId);
        long currentTime = System.currentTimeMillis();
        return (int) ((currentTime - queryTime) / 1000);
    }
}
