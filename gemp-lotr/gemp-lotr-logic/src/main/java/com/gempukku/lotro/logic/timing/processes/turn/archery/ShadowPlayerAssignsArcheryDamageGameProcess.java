package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
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
    public void process(LotroGame game) {
        if (_woundsToAssign > 0) {
            Filter filter =
                    Filters.and(
                            CardType.MINION,
                            Filters.owner(_playerId),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    return modifiersQuerying.canTakeArcheryWound(gameState, physicalCard);
                                }
                            });

            SystemQueueAction action = new SystemQueueAction();
            for (int i = 0; i < _woundsToAssign; i++) {
                final int woundsLeft = _woundsToAssign - i;
                ChooseAndWoundCharactersEffect woundCharacter = new ChooseAndWoundCharactersEffect(action, _playerId, 1, 1, filter);
                woundCharacter.setChoiceText("Choose minion to assign archery wound to - remaining wounds: " + woundsLeft);
                action.appendEffect(woundCharacter);
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
