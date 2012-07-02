package com.gempukku.lotro.game;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.logic.timing.GameResultListener;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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
    private GameHistoryService _gameHistoryService;

    private DefaultCardCollection _defaultCollection;
    private CountDownLatch _collectionReadyLatch = new CountDownLatch(1);

    private ChatServer _chatServer;
    private boolean _test;
    private GameRecorder _gameRecorder;

    public LotroServer(DeckDAO deckDao, GameHistoryService gameHistoryService, LotroCardBlueprintLibrary library, ChatServer chatServer, boolean test) {
        _deckDao = deckDao;
        _gameHistoryService = gameHistoryService;
        _lotroCardBlueprintLibrary = library;
        _chatServer = chatServer;
        _test = test;
        _defaultCollection = new DefaultCardCollection();

        // Hunters have 1-194 normal cards, 9 "O" cards, and 3 extra to cover the different culture versions of 15_60

        Thread thr = new Thread() {
            public void run() {
                final int[] cardCounts = new int[]{129, 365, 122, 122, 365, 128, 128, 365, 122, 52, 122, 266, 203, 203, 15, 207, 6, 157, 149, 40};

                for (int i = 0; i <= 19; i++) {
                    System.out.println("Loading set " + i);
                    for (int j = 1; j <= cardCounts[i]; j++) {
                        String blueprintId = i + "_" + j;
                        try {
                            if (_lotroCardBlueprintLibrary.getBaseBlueprintId(blueprintId).equals(blueprintId)) {
                                LotroCardBlueprint cardBlueprint = _lotroCardBlueprintLibrary.getLotroCardBlueprint(blueprintId);
                                CardType cardType = cardBlueprint.getCardType();
                                if (cardType == CardType.SITE || cardType == CardType.THE_ONE_RING)
                                    _defaultCollection.addItem(blueprintId, 1);
                                else
                                    _defaultCollection.addItem(blueprintId, 4);
                            }
                        } catch (IllegalArgumentException exp) {

                        }
                    }
                }
                _collectionReadyLatch.countDown();
            }
        };
        thr.start();

        _gameRecorder = new GameRecorder(_gameHistoryService);
    }

    public InputStream getGameRecording(String playerId, String gameId) throws IOException {
        return _gameRecorder.getRecordedGame(playerId, gameId);
    }

    public CardCollection getDefaultCollection() {
        try {
            _collectionReadyLatch.await();
        } catch (InterruptedException exp) {
            throw new RuntimeException("Error while awaiting loading a default colleciton", exp);
        }
        return _defaultCollection;
    }

    protected void cleanup() {
        long currentTime = System.currentTimeMillis();

        synchronized (_finishedGamesTime) {
            LinkedHashMap<String, Date> copy = new LinkedHashMap<String, Date>(_finishedGamesTime);
            for (Map.Entry<String, Date> finishedGame : copy.entrySet()) {
                String gameId = finishedGame.getKey();
                if (currentTime > finishedGame.getValue().getTime() + _timeToGameDeathWarning
                        && !_gameDeathWarningsSent.contains(gameId)) {
                    _chatServer.getChatRoom(getChatRoomName(gameId)).sendMessage("System", "This game is already finished and will be shortly removed, please move to the Game Hall");
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
        }

        for (LotroGameMediator lotroGameMediator : _runningGames.values())
            lotroGameMediator.cleanup();
    }

    private String getChatRoomName(String gameId) {
        return "Game" + gameId;
    }

    public synchronized String createNewGame(LotroFormat lotroFormat, String tournament, final LotroGameParticipant[] participants, boolean competetive) {
        if (participants.length < 2)
            throw new IllegalArgumentException("There has to be at least two players");
        final String gameId = String.valueOf(_nextGameId);

        boolean noSpectators = false;

        if (noSpectators) {
            Set<String> allowedUsers = new HashSet<String>();
            for (LotroGameParticipant participant : participants)
                allowedUsers.add(participant.getPlayerId());
            _chatServer.createPrivateChatRoom(getChatRoomName(gameId), allowedUsers, 30);
        } else
            _chatServer.createChatRoom(getChatRoomName(gameId), 30);

        LotroGameMediator lotroGameMediator = new LotroGameMediator(lotroFormat, participants, _lotroCardBlueprintLibrary,
                competetive ? 60 * 40 : 60 * 80, noSpectators);
        lotroGameMediator.addGameResultListener(
                new GameResultListener() {
                    @Override
                    public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                        synchronized (_finishedGamesTime) {
                            _finishedGamesTime.put(gameId, new Date());
                        }
                    }

                    @Override
                    public void gameCancelled() {
                        synchronized (_finishedGamesTime) {
                            _finishedGamesTime.put(gameId, new Date());
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

        final GameRecorder.GameRecordingInProgress gameRecordingInProgress = _gameRecorder.recordGame(lotroGameMediator, lotroFormat.getName(), tournament, deckNames);
        lotroGameMediator.addGameResultListener(
                new GameResultListener() {
                    @Override
                    public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                        final Map.Entry<String, String> loserEntry = loserPlayerIdsWithReasons.entrySet().iterator().next();

                        gameRecordingInProgress.finishRecording(winnerPlayerId, winReason, loserEntry.getKey(), loserEntry.getValue());
                    }

                    @Override
                    public void gameCancelled() {
                        gameRecordingInProgress.finishRecording(participants[0].getPlayerId(), "Game cancelled due to error", participants[1].getPlayerId(), "Game cancelled due to error");
                    }
                }
        );

        _runningGames.put(gameId, lotroGameMediator);
        _nextGameId++;
        return gameId;
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
        return _runningGames.get(gameId);
    }
}

