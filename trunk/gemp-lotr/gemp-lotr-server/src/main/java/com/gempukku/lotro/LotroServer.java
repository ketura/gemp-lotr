package com.gempukku.lotro;

import com.gempukku.lotro.cards.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class LotroServer {
    private static final Logger log = Logger.getLogger(LotroServer.class);

    private LotroCardBlueprintLibrary _lotroCardBlueprintLibrary = new LotroCardBlueprintLibrary();

    private Map<String, LotroGameMediator> _runningGames = new HashMap<String, LotroGameMediator>();

    private boolean _started;
    private CleaningTask _cleaningTask;
    private int _nextGameId = 1;

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
        LotroGameMediator lotroGameMediator = new LotroGameMediator(lotroFormat, participants);
//        String gameId = String.valueOf(_nextGameId);
        _runningGames.put(gameId, lotroGameMediator);
        _nextGameId++;
        return gameId;
    }

    public LotroDeck getParticipantDeck(String participantId) {
        LotroDeck lotroDeck = new LotroDeck();
        lotroDeck.setRing(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_1"));
        lotroDeck.setRingBearer(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_290"));

        for (int i = 3; i < 58; i++)
            lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_" + i));

        // Sites
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_326"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_331"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_337"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_346"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_349"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_351"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_354"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_356"));
        lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_362"));

        for (int i = 0; i < 4; i++) {
            lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_145"));
            lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_146"));
            lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_148"));
            lotroDeck.addCard(_lotroCardBlueprintLibrary.getLotroCardBlueprint("1_149"));
        }

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
