package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.timing.PlayerOrderFeedback;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class BiddingGameProcess implements GameProcess {
    private static final Logger logger = Logger.getLogger(BiddingGameProcess.class);
    private final Set<String> _players;
    private final PlayerOrderFeedback _playerOrderFeedback;
    private final Map<String, Integer> _bids = new LinkedHashMap<>();

    public BiddingGameProcess(Set<String> players, PlayerOrderFeedback playerOrderFeedback) {
        _players = players;
        _playerOrderFeedback = playerOrderFeedback;
    }

    @Override
    public void process(LotroGame game) {
        logger.debug("BiddingGameProcess - process called");
        logger.debug("Naming players:");
        for (String player: _players) {
            logger.debug(player);
        }
        for (String player : _players) {
            final String decidingPlayer = player;
            game.getUserFeedback().sendAwaitingDecision(decidingPlayer, new IntegerAwaitingDecision(1, "Choose a number of burdens to bid", 0) {
                @Override
                public void decisionMade(String result) throws DecisionResultInvalidException {
                    try {
                        int bid = Integer.parseInt(result);
                        if (bid < 0)
                            throw new DecisionResultInvalidException();
                        playerPlacedBid(decidingPlayer, bid);
                    } catch (NumberFormatException exp) {
                        throw new DecisionResultInvalidException();
                    }
                }
            });
        }
    }

    private void playerPlacedBid(String playerId, int bid) {
        _bids.put(playerId, bid);
    }

    @Override
    public GameProcess getNextProcess() {
        return new ChooseSeatingOrderGameProcess(_bids, _playerOrderFeedback);
    }
}
