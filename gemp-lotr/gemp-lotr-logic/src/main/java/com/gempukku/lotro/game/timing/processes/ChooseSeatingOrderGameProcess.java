package com.gempukku.lotro.game.timing.processes;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.PlayerOrder;
import com.gempukku.lotro.game.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.game.timing.PlayerOrderFeedback;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.PlayersDrawStartingHandGameProcess;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseSeatingOrderGameProcess implements GameProcess {
    private final String[] _choices = new String[]{"first", "second", "third", "fourth", "fifth"};
    private final Map<String, Integer> _bids;
    private final PlayerOrderFeedback _playerOrderFeedback;

    private final Iterator<String> _biddingOrderPlayers;
    private final String[] _orderedPlayers;
    private boolean _sentBids;

    public ChooseSeatingOrderGameProcess(Map<String, Integer> bids, PlayerOrderFeedback playerOrderFeedback) {
        _bids = bids;
        _playerOrderFeedback = playerOrderFeedback;

        ArrayList<String> participantList = new ArrayList<>(bids.keySet());
        Collections.shuffle(participantList, ThreadLocalRandom.current());

        participantList.sort(new Comparator<>() {
            @Override
            public int compare(String o1, String o2) {
                return _bids.get(o2) - _bids.get(o1);
            }
        });

        _biddingOrderPlayers = participantList.iterator();
        _orderedPlayers = new String[participantList.size()];
    }

    @Override
    public void process(DefaultGame game) {
        if (!_sentBids) {
            _sentBids = true;
            for (Map.Entry<String, Integer> playerBid : _bids.entrySet())
                game.getGameState().sendMessage(playerBid.getKey() + " bid " + playerBid.getValue());
        }
        checkForNextSeating(game);
    }

    private int getLastEmptySeat() {
        boolean found = false;
        int emptySeatIndex = -1;
        for (int i = 0; i < _orderedPlayers.length; i++) {
            if (_orderedPlayers[i] == null) {
                if (found)
                    return -1;
                found = true;
                emptySeatIndex = i;
            }
        }
        return emptySeatIndex;
    }

    private void checkForNextSeating(DefaultGame game) {
        int lastEmptySeat = getLastEmptySeat();
        if (lastEmptySeat == -1)
            askNextPlayerToChoosePlace(game);
        else {
            _orderedPlayers[lastEmptySeat] = _biddingOrderPlayers.next();
            _playerOrderFeedback.setPlayerOrder(new PlayerOrder(Arrays.asList(_orderedPlayers)), _orderedPlayers[0]);
        }
    }

    private String[] getEmptySeatNumbers() {
        List<String> result = new LinkedList<>();
        for (int i = 0; i < _orderedPlayers.length; i++)
            if (_orderedPlayers[i] == null)
                result.add("Go " + _choices[i]);
        return result.toArray(new String[result.size()]);
    }

    private void participantHasChosenSeat(DefaultGame game, String participant, int placeIndex) {
        _orderedPlayers[placeIndex] = participant;

        checkForNextSeating(game);
    }

    private void askNextPlayerToChoosePlace(final DefaultGame game) {
        final String playerId = _biddingOrderPlayers.next();
        game.getUserFeedback().sendAwaitingDecision(playerId,
                new MultipleChoiceAwaitingDecision(1, "Choose one", getEmptySeatNumbers()) {
                    @Override
                    protected void validDecisionMade(int index, String result) {
                        game.getGameState().sendMessage(playerId + " has chosen to go " + _choices[index]);
                        participantHasChosenSeat(game, playerId, index);
                    }
                }
        );
    }

    @Override
    public GameProcess getNextProcess() {
        return new PlayersDrawStartingHandGameProcess(_orderedPlayers[0]);
    }
}
