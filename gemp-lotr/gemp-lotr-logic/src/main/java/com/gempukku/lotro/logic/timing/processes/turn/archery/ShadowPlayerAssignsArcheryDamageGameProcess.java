package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ShadowPlayerAssignsArcheryDamageGameProcess implements GameProcess {
    private LotroGame _game;
    private String _playerId;
    private int _woundsToAssign;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public ShadowPlayerAssignsArcheryDamageGameProcess(LotroGame game, String playerId, int woundsToAssign, GameProcess followingGameProcess) {
        _game = game;
        _playerId = playerId;
        _woundsToAssign = woundsToAssign;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        if (_woundsToAssign > 0) {
            Filter filter = Filters.and(Filters.type(CardType.MINION), Filters.owner(_playerId));

            RequiredTriggerAction action = new RequiredTriggerAction(null);
            for (int i = 0; i < _woundsToAssign; i++) {
                final int woundsLeft = _woundsToAssign - i;
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, _playerId, 1, 1, filter) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Choose minion to assign archery wound to - remaining wounds: " + woundsLeft;
                            }
                        });
            }

            _game.getActionsEnvironment().addActionToStack(action);
        }
        _nextProcess = _followingGameProcess;
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
