package com.gempukku.lotro.game;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.logic.timing.GameResultListener;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LotroServer extends AbstractServer {
    private static final Logger log = Logger.getLogger(LotroServer.class);

    private LotroCardBlueprintLibrary _lotroCardBlueprintLibrary;

    private Map<String, LotroGameMediator> _runningGames = new ConcurrentHashMap<String, LotroGameMediator>();
    private Set<String> _gameDeathWarningsSent = new HashSet<String>();

    private final Map<String, Date> _finishedGamesTime = new LinkedHashMap<String, Date>();
    private final long _timeToGameDeath = 1000 * 60 * 5; // 5 minutes
    private final long _timeToGameDeathWarning = 1000 * 60 * 4; // 4 minutes

    private int _nextGameId = 1;

    private DeckDAO _deckDao;

    private ChatServer _chatServer;
    private GameRecorder _gameRecorder;

    private ReadWriteLock _lock = new ReentrantReadWriteLock();

    public LotroServer(DeckDAO deckDao, LotroCardBlueprintLibrary library, ChatServer chatServer, GameRecorder gameRecorder) {
        _deckDao = deckDao;
        _lotroCardBlueprintLibrary = library;
        _chatServer = chatServer;
        _gameRecorder = gameRecorder;
    }

    protected void cleanup() {
        _lock.writeLock().lock();
        try {
            long currentTime = System.currentTimeMillis();

            LinkedHashMap<String, Date> copy = new LinkedHashMap<String, Date>(_finishedGamesTime);
            for (Map.Entry<String, Date> finishedGame : copy.entrySet()) {
                String gameId = finishedGame.getKey();
                if (currentTime > finishedGame.getValue().getTime() + _timeToGameDeathWarning
                        && !_gameDeathWarningsSent.contains(gameId)) {
                    _chatServer.getChatRoom(getChatRoomName(gameId)).sendMessage("System", "This game is already finished and will be shortly removed, please move to the Game Hall", true);
                    _gameDeathWarningsSent.add(gameId);
                }
                if (currentTime > finishedGame.getValue().getTime() + _timeToGameDeath) {
                    _gameDeathWarningsSent.remove(gameId);
                    _runningGames.remove(gameId);
                    _chatServer.destroyChatRoom(getChatRoomName(gameId));
                    _finishedGamesTime.remove(gameId);
                } else {
                    break;
                }
            }

            for (LotroGameMediator lotroGameMediator : _runningGames.values())
                lotroGameMediator.cleanup();
        } finally {
            _lock.writeLock().unlock();
        }
    }

    private String getChatRoomName(String gameId) {
        return "Game" + gameId;
    }

    public String createNewGame(LotroFormat lotroFormat, String tournamentName, final LotroGameParticipant[] participants, boolean allowSpectators, boolean allowCancelling, boolean muteSpectators, boolean competitiveTime) {
        _lock.writeLock().lock();
        try {
            if (participants.length < 2)
                throw new IllegalArgumentException("There has to be at least two players");
            final String gameId = String.valueOf(_nextGameId);

            if (muteSpectators) {
                Set<String> allowedUsers = new HashSet<String>();
                for (LotroGameParticipant participant : participants)
                    allowedUsers.add(participant.getPlayerId());
                _chatServer.createVoicedChatRoom(getChatRoomName(gameId), allowedUsers, 30);
            } else
                _chatServer.createChatRoom(getChatRoomName(gameId), 30);

            LotroGameMediator lotroGameMediator = new LotroGameMediator(lotroFormat, participants, _lotroCardBlueprintLibrary,
                    competitiveTime ? 60 * 40 : 60 * 80, allowSpectators, allowCancelling);
            lotroGameMediator.addGameResultListener(
                    new GameResultListener() {
                        @Override
                        public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                            _lock.writeLock().lock();
                            try {
                                _finishedGamesTime.put(gameId, new Date());
                            } finally {
                                _lock.writeLock().unlock();
                            }
                        }

                        @Override
                        public void gameCancelled() {
                            _lock.writeLock().lock();
                            try {
                                _finishedGamesTime.put(gameId, new Date());
                            } finally {
                                _lock.writeLock().unlock();
                            }
                        }
                    });
            lotroGameMediator.sendMessageToPlayers("You're starting a game of " + lotroFormat.getName());

            StringBuffer players = new StringBuffer();
            Map<String, String> deckNames = new HashMap<String, String>();
            for (LotroGameParticipant participant : participants) {
                deckNames.put(participant.getPlayerId(), participant.getDeck().getDeckName());
                if (players.length() > 0)
                    players.append(", ");
                players.append(participant.getPlayerId());
            }

            lotroGameMediator.sendMessageToPlayers("Players in the game are: " + players.toString());

            final GameRecorder.GameRecordingInProgress gameRecordingInProgress = _gameRecorder.recordGame(lotroGameMediator, lotroFormat.getName(), tournamentName, deckNames);
            lotroGameMediator.addGameResultListener(
                    new GameResultListener() {
                        @Override
                        public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                            _lock.writeLock().lock();
                            try {
                                final Map.Entry<String, String> loserEntry = loserPlayerIdsWithReasons.entrySet().iterator().next();

                                gameRecordingInProgress.finishRecording(winnerPlayerId, winReason, loserEntry.getKey(), loserEntry.getValue());
                            } finally {
                                _lock.writeLock().unlock();
                            }
                        }

                        @Override
                        public void gameCancelled() {
                            _lock.writeLock().lock();
                            try {
                                gameRecordingInProgress.finishRecording(participants[0].getPlayerId(), "Game cancelled due to error", participants[1].getPlayerId(), "Game cancelled due to error");
                            } finally {
                                _lock.writeLock().unlock();
                            }
                        }
                    }
            );

            _runningGames.put(gameId, lotroGameMediator);
            _nextGameId++;
            return gameId;
        } finally {
            _lock.writeLock().unlock();
        }
    }

    public LotroDeck getParticipantDeck(Player player, String deckName) {
        return _deckDao.getDeckForPlayer(player, deckName);
    }

    public LotroDeck createDeckWithValidate(String deckName, String contents) {
        if (contents.contains("|")) {
            // New format
            int cnt = 0;
            for (char c : contents.toCharArray()) {
                if (c == '|')
                    cnt++;
            }

            if (cnt != 3)
                return null;

            return _deckDao.buildDeckFromContents(deckName, contents);
        } else {
            // Old format
            List<String> cards = Arrays.asList(contents.split(","));
            if (cards.size() < 2)
                return null;

            return _deckDao.buildDeckFromContents(deckName, contents);
        }
    }

    public LotroGameMediator getGameById(String gameId) {
        _lock.readLock().lock();
        try {
            return _runningGames.get(gameId);
        } finally {
            _lock.readLock().unlock();
        }
    }
}

