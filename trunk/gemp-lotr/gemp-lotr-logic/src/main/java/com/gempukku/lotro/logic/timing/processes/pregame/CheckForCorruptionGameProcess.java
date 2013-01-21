package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.PlayerOrderFeedback;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Map;

public class CheckForCorruptionGameProcess implements GameProcess {
    private Map<String, Integer> _bids;
    private PlayerOrderFeedback _playerOrderFeedback;

    public CheckForCorruptionGameProcess(Map<String, Integer> bids, PlayerOrderFeedback playerOrderFeedback) {
        _bids = bids;
        _playerOrderFeedback = playerOrderFeedback;
    }

    @Override
    public void process(LotroGame game) {
        for (Map.Entry<String, Integer> playerBid : _bids.entrySet()) {
            String player = playerBid.getKey();
            int bid = playerBid.getValue();

            game.getGameState().startPlayerTurn(player);
            int ringBearerResistance = game.getModifiersQuerying().getResistance(game.getGameState(), game.getGameState().getRingBearer(player));
            if (ringBearerResistance<=bid) {
                game.playerLost(player, "Corrupted before game started");
                break;
            }
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return new ChooseSeatingOrderGameProcess(_bids, _playerOrderFeedback);
    }
}
