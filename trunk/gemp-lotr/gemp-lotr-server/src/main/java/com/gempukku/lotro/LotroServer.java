package com.gempukku.lotro;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.Deck;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LotroServer {
    private static final Logger log = Logger.getLogger(LotroServer.class);

    private LotroCardBlueprintLibrary _lotroCardBlueprintLibrary = new LotroCardBlueprintLibrary();

    private Map<String, LotroGameMediator> _runningGames = new HashMap<String, LotroGameMediator>();

    private boolean _started;
    private CleaningTask _cleaningTask;
    private int _nextGameId = 1;

    private PlayerDAO _playerDao;
    private DeckDAO _deckDao;
    private DefaultCardCollection _defaultCollection;

    public LotroServer() {
        _defaultCollection = new DefaultCardCollection();
        for (int i = 1; i <= 1; i++) {
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

        DbAccess dbAccess = new DbAccess();
        _playerDao = new PlayerDAO(dbAccess);
        _deckDao = new DeckDAO(dbAccess);
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

    public synchronized void startServer() {
        if (!_started) {
            _cleaningTask = new CleaningTask();
            new Thread(_cleaningTask).start();
            _started = true;
        }
    }

    public synchronized void stopServer() {
        if (_started) {
            _cleaningTask.stop();
            _started = false;
        }
    }

    public void cleanup() {
        // TODO
    }

    public synchronized String createNewGame(LotroFormat lotroFormat, LotroGameParticipant[] participants, String gameId) {
        if (participants.length < 2)
            throw new IllegalArgumentException("There has to be at least two players");
        LotroGameMediator lotroGameMediator = new LotroGameMediator(lotroFormat, participants, _lotroCardBlueprintLibrary);
//        String gameId = String.valueOf(_nextGameId);
        _runningGames.put(gameId, lotroGameMediator);
        _nextGameId++;
        return gameId;
    }

    public Deck validateDeck(String contents) {
        List<String> cards = Arrays.asList(contents.split(","));
        if (cards.size() < 11)
            return null;

        try {
            String ringBearer = cards.get(0);
            if (!_lotroCardBlueprintLibrary.getLotroCardBlueprint(ringBearer).hasKeyword(Keyword.RING_BEARER))
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

            return new Deck(ringBearer, ring, cards.subList(2, 11), cards.subList(11, cards.size()));
        } catch (IllegalArgumentException exp) {
            return null;
        }
    }

    public LotroDeck getParticipantDeck(String participantId) {
        Player player = _playerDao.getPlayer(participantId);
        Deck deck = _deckDao.getDeckForPlayer(player, "default");
        if (deck == null)
            return null;

        LotroDeck lotroDeck = new LotroDeck();
        lotroDeck.setRing(deck.getRing());
        lotroDeck.setRingBearer(deck.getRingBearer());
        for (String site : deck.getSites())
            lotroDeck.addSite(site);
        for (String card : deck.getDeck())
            lotroDeck.addCard(card);

        return lotroDeck;
    }

    public LotroGameMediator getGameById(String gameId) {
        return _runningGames.get(gameId);
    }

    private class CleaningTask implements Runnable {
        private boolean _running = true;

        public void run() {
            while (_running) {
                cleanup();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("Cleaning task interrupted", e);
                }
            }
        }

        public void stop() {
            _running = false;
        }
    }
}
