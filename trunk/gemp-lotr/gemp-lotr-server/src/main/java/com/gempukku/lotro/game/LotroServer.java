package com.gempukku.lotro.game;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.GameHistoryDAO;
import com.gempukku.lotro.db.vo.DeckVO;
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
    private final long _timeToGameDeath = 1000 * 60 * 10; // 10 minutes
    private final long _timeToGameDeathWarning = 1000 * 60 * 9; // 9 minutes

    private int _nextGameId = 1;

    private DeckDAO _deckDao;
    private GameHistoryDAO _gameHistoryDao;

    private DefaultCardCollection _defaultCollection;
    private CountDownLatch _collectionReadyLatch = new CountDownLatch(1);

    private ChatServer _chatServer;
    private boolean _test;
    private GameRecorder _gameRecorder;

    public LotroServer(DeckDAO deckDao, GameHistoryDAO gameHistoryDao, LotroCardBlueprintLibrary library, ChatServer chatServer, boolean test) {
        _deckDao = deckDao;
        _gameHistoryDao = gameHistoryDao;
        _lotroCardBlueprintLibrary = library;
        _chatServer = chatServer;
        _test = test;
        _defaultCollection = new DefaultCardCollection();

        final int[] cardCounts = new int[]{129, 365, 122, 122, 365, 128, 128, 365, 122, 52, 122, 266, 194, 194};

        Thread thr = new Thread(
                new Runnable() {
                    public void run() {
                        for (int i = 0; i <= 12; i++) {
                            for (int j = 1; j <= cardCounts[i]; j++) {
                                String blueprintId = i + "_" + j;
                                try {
                                    LotroCardBlueprint cardBlueprint = _lotroCardBlueprintLibrary.getLotroCardBlueprint(blueprintId);
                                    CardType cardType = cardBlueprint.getCardType();
                                    if (cardType == CardType.SITE || cardType == CardType.THE_ONE_RING)
                                        _defaultCollection.addItem(blueprintId, 1);
                                    else
                                        _defaultCollection.addItem(blueprintId, 4);
                                } catch (IllegalArgumentException exp) {

                                }
                            }
                        }
                        _collectionReadyLatch.countDown();
                    }
                }
        );
        thr.start();

        _gameRecorder = new GameRecorder(_gameHistoryDao);
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
                    log.debug("Removing stale game: " + gameId);
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

    public synchronized String createNewGame(LotroFormat lotroFormat, LotroGameParticipant[] participants, boolean competetive) {
        if (participants.length < 2)
            throw new IllegalArgumentException("There has to be at least two players");
        final String gameId = String.valueOf(_nextGameId);

        _chatServer.createChatRoom(getChatRoomName(gameId), 30);

        LotroGameMediator lotroGameMediator = new LotroGameMediator(lotroFormat, participants, _lotroCardBlueprintLibrary, competetive ? 60 * 40 : 60 * 80);
        lotroGameMediator.addGameResultListener(
                new GameResultListener() {
                    @Override
                    public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                        log.debug("Game finished, winner is - " + winnerPlayerId + " due to: " + winReason);
                        synchronized (_finishedGamesTime) {
                            _finishedGamesTime.put(gameId, new Date());
                        }
                    }
                });
        lotroGameMediator.sendMessageToPlayers("You're starting a game of " + lotroFormat.getName());

        Map<String, String> deckNames = new HashMap<String, String>();
        for (LotroGameParticipant participant : participants)
            deckNames.put(participant.getPlayerId(), participant.getDeckName());

        final GameRecorder.GameRecordingInProgress gameRecordingInProgress = _gameRecorder.recordGame(lotroGameMediator, lotroFormat.getName(), deckNames);
        lotroGameMediator.addGameResultListener(
                new GameResultListener() {
                    @Override
                    public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                        final Map.Entry<String, String> loserEntry = loserPlayerIdsWithReasons.entrySet().iterator().next();

                        gameRecordingInProgress.finishRecording(winnerPlayerId, winReason, loserEntry.getKey(), loserEntry.getValue());
                    }
                }
        );

        _runningGames.put(gameId, lotroGameMediator);
        _nextGameId++;
        return gameId;
    }

    private DeckVO validateDeck(String contents) {
        List<String> cards = Arrays.asList(contents.split(","));
        if (cards.size() < 2)
            return null;

        try {
            String ringBearer = cards.get(0);
            if (!_lotroCardBlueprintLibrary.getLotroCardBlueprint(ringBearer).hasKeyword(Keyword.CAN_START_WITH_RING))
                return null;

            String ring = cards.get(1);
            if (_lotroCardBlueprintLibrary.getLotroCardBlueprint(ring).getCardType() != CardType.THE_ONE_RING)
                return null;

            return new DeckVO(ringBearer, ring, cards.subList(2, cards.size()));
        } catch (IllegalArgumentException exp) {
            return null;
        }
    }

    public LotroDeck getParticipantDeck(Player player, String deckName) {
        DeckVO deck = _deckDao.getDeckForPlayer(player, deckName);
        if (deck == null)
            return null;

        return convertDeck(deck);
    }

    public LotroDeck savePlayerDeck(Player player, String deckName, String contents) {
        DeckVO deck = validateDeck(contents);
        if (deck == null)
            return null;
        _deckDao.saveDeckForPlayer(player, deckName, deck);
        return convertDeck(deck);
    }

    public LotroDeck renamePlayerDeck(Player player, String oldDeckName, String newDeckName) {
        DeckVO deck = _deckDao.renameDeck(player, oldDeckName, newDeckName);
        if (deck == null)
            return null;
        return convertDeck(deck);
    }

    public LotroDeck createTemporaryDeckForPlayer(Player player, String contents) {
        DeckVO deck = validateDeck(contents);
        if (deck == null)
            return null;
        return convertDeck(deck);
    }

    private LotroDeck convertDeck(DeckVO deck) {
        LotroDeck lotroDeck = new LotroDeck();
        lotroDeck.setRing(deck.getRing());
        lotroDeck.setRingBearer(deck.getRingBearer());
        for (String card : deck.getCards()) {
            LotroCardBlueprint cardBlueprint = _lotroCardBlueprintLibrary.getLotroCardBlueprint(card);
            if (cardBlueprint.getCardType() == CardType.SITE)
                lotroDeck.addSite(card);
            else
                lotroDeck.addCard(card);
        }

        return lotroDeck;
    }

    public LotroGameMediator getGameById(String gameId) {
        return _runningGames.get(gameId);
    }
}
