package com.gempukku.lotro.game;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.GameHistoryDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.logic.timing.GameResultListener;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LotroServer extends AbstractServer {
    private static final Logger log = Logger.getLogger(LotroServer.class);

    private LotroCardBlueprintLibrary _lotroCardBlueprintLibrary;

    private Map<String, LotroGameMediator> _runningGames = new ConcurrentHashMap<String, LotroGameMediator>();

    private final Map<String, Date> _finishedGamesTime = new LinkedHashMap<String, Date>();
    private final long _timeToGameDeath = 1000 * 60 * 10; // 10 minutes

    private int _nextGameId = 1;

    private PlayerDAO _playerDao;
    private DeckDAO _deckDao;
    private GameHistoryDAO _gameHistoryDao;
    private DefaultCardCollection _defaultCollection;
    private ChatServer _chatServer;
    private GameRecorder _gameRecorder;

    public LotroServer(DbAccess dbAccess, LotroCardBlueprintLibrary library, ChatServer chatServer, boolean test) {
        _lotroCardBlueprintLibrary = library;
        _chatServer = chatServer;
        _defaultCollection = new DefaultCardCollection(library);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 365; j++) {
                String blueprintId = i + "_" + j;
                try {
                    LotroCardBlueprint cardBlueprint = _lotroCardBlueprintLibrary.getLotroCardBlueprint(blueprintId);
                    CardType cardType = cardBlueprint.getCardType();
                    if (cardType == CardType.SITE || cardType == CardType.THE_ONE_RING)
                        _defaultCollection.addCards(blueprintId, cardBlueprint, 1);
                    else
                        _defaultCollection.addCards(blueprintId, cardBlueprint, 4);
                } catch (IllegalArgumentException exp) {

                }
            }
        }

        if (test) {
            for (int i = 9; i <= 9; i++) {
                for (int j = 1; j <= 365; j++) {
                    String blueprintId = i + "_" + j;
                    try {
                        LotroCardBlueprint cardBlueprint = _lotroCardBlueprintLibrary.getLotroCardBlueprint(blueprintId);
                        CardType cardType = cardBlueprint.getCardType();
                        if (cardType == CardType.SITE || cardType == CardType.THE_ONE_RING)
                            _defaultCollection.addCards(blueprintId, cardBlueprint, 1);
                        else
                            _defaultCollection.addCards(blueprintId, cardBlueprint, 4);
                    } catch (IllegalArgumentException exp) {

                    }
                }
            }
        }

        _playerDao = new PlayerDAO(dbAccess);
        _deckDao = new DeckDAO(dbAccess);

        _gameHistoryDao = new GameHistoryDAO(dbAccess);

        _gameRecorder = new GameRecorder(_gameHistoryDao);
    }

    public InputStream getGameRecording(String playerId, String gameId) throws IOException {
        return _gameRecorder.getRecordedGame(playerId, gameId);
    }

    public List<GameHistoryEntry> getPlayerGameHistory(Player player, int start, int count) {
        return _gameHistoryDao.getGameHistoryForPlayer(player, start, count);
    }

    public int getPlayerGameHistoryRecordCount(Player player) {
        return _gameHistoryDao.getGameHistoryForPlayerCount(player);
    }

    public int getGamesPlayedCountInLastMs(long ms) {
        return _gameHistoryDao.getGamesPlayedCountInLastMs(ms);
    }

    public LotroCardBlueprintLibrary getLotroCardBlueprintLibrary() {
        return _lotroCardBlueprintLibrary;
    }

    public PlayerDAO getPlayerDao() {
        return _playerDao;
    }

    public DeckDAO getDeckDao() {
        return _deckDao;
    }

    public CardCollection getDefaultCollection() {
        return _defaultCollection;
    }

    protected void cleanup() {
        long currentTime = System.currentTimeMillis();

        synchronized (_finishedGamesTime) {
            LinkedHashMap<String, Date> copy = new LinkedHashMap<String, Date>(_finishedGamesTime);
            for (Map.Entry<String, Date> finishedGame : copy.entrySet()) {
                if (currentTime > finishedGame.getValue().getTime() + _timeToGameDeath) {
                    String gameId = finishedGame.getKey();
                    log.debug("Removing stale game: " + gameId);
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

    public synchronized String createNewGame(LotroFormat lotroFormat, String formatName, LotroGameParticipant[] participants) {
        if (participants.length < 2)
            throw new IllegalArgumentException("There has to be at least two players");
        final String gameId = String.valueOf(_nextGameId);
        String chatRoomName = getChatRoomName(gameId);

        ChatRoomMediator room = _chatServer.createChatRoom(chatRoomName);
        room.sendMessage("System", "You're starting a game of " + formatName);

        LotroGameMediator lotroGameMediator = new LotroGameMediator(lotroFormat, participants, _lotroCardBlueprintLibrary);
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
        final GameRecorder.GameRecordingInProgress gameRecordingInProgress = _gameRecorder.recordGame(lotroGameMediator);
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

    public LotroDeck validateDeck(String contents) {
        List<String> cards = Arrays.asList(contents.split(","));
        if (cards.size() < 11)
            return null;

        try {
            String ringBearer = cards.get(0);
            if (!_lotroCardBlueprintLibrary.getLotroCardBlueprint(ringBearer).hasKeyword(Keyword.CAN_START_WITH_RING))
                return null;

            String ring = cards.get(1);
            if (_lotroCardBlueprintLibrary.getLotroCardBlueprint(ring).getCardType() != CardType.THE_ONE_RING)
                return null;

            int index = 1;
            for (String site : cards.subList(2, 11)) {
                LotroCardBlueprint siteBlueprint = _lotroCardBlueprintLibrary.getLotroCardBlueprint(site);
                if (siteBlueprint.getCardType() != CardType.SITE || siteBlueprint.getSiteNumber() != index)
                    return null;
                index++;
            }

            for (String card : cards.subList(11, cards.size()))
                _lotroCardBlueprintLibrary.getLotroCardBlueprint(card);

            LotroDeck deck = new LotroDeck();
            deck.setRingBearer(cards.get(0));
            deck.setRing(cards.get(1));
            for (int i = 2; i < 11; i++)
                deck.addSite(cards.get(i));
            for (int i = 11; i < cards.size(); i++)
                deck.addCard(cards.get(i));
            return deck;
        } catch (IllegalArgumentException exp) {
            return null;
        }
    }

    public LotroDeck getParticipantDeck(String participantId, String deckName) {
        Player player = _playerDao.getPlayer(participantId);
        LotroDeck deck = _deckDao.getDeckForPlayer(player, deckName);
        if (deck == null)
            return null;

        LotroDeck lotroDeck = new LotroDeck();
        lotroDeck.setRing(deck.getRing());
        lotroDeck.setRingBearer(deck.getRingBearer());
        for (String site : deck.getSites())
            lotroDeck.addSite(site);
        for (String card : deck.getAdventureCards())
            lotroDeck.addCard(card);

        return lotroDeck;
    }

    public LotroGameMediator getGameById(String gameId) {
        return _runningGames.get(gameId);
    }
}
