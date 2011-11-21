package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Map;

public class FirstPlayerPlaysSiteGameProcess implements GameProcess {
    private Map<String, Integer> _bids;
    private String _firstPlayer;

    public FirstPlayerPlaysSiteGameProcess(Map<String, Integer> bids, String firstPlayer) {
        _bids = bids;
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process(LotroGame game) {
        SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new PlaySiteEffect(_firstPlayer, null, 1));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        for (String playerId : game.getGameState().getPlayerOrder().getAllPlayers())
                            game.getGameState().setPlayerPosition(playerId, 1);
                    }
                }
        );
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return new PlayRingBearerRingAndAddBurdersGameProcess(_bids, _firstPlayer);
    }
}
