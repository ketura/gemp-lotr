package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class FellowshipPlayerAssignsArcheryDamageGameProcess implements GameProcess {
    private LotroGame _game;
    private int _woundsToAssign;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public FellowshipPlayerAssignsArcheryDamageGameProcess(LotroGame game, int woundsToAssign, GameProcess followingGameProcess) {
        _game = game;
        _woundsToAssign = woundsToAssign;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        if (_woundsToAssign > 0) {

            Filter filter = Filters.or(
                    Filters.type(CardType.COMPANION),
                    new Filter() {
                        @Override
                        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                            return modifiersQuerying.isAllyOnCurrentSite(gameState, physicalCard);
                        }
                    });

            RequiredTriggerAction action = new RequiredTriggerAction(null) {
                @Override
                public String getText(LotroGame game) {
                    return "Archery fire";
                }
            };
            for (int i = 0; i < _woundsToAssign; i++) {
                final int woundsLeft = _woundsToAssign - i;
                ChooseAndWoundCharactersEffect woundCharacter = new ChooseAndWoundCharactersEffect(action, _game.getGameState().getCurrentPlayerId(), 1, 1, filter);
                woundCharacter.setChoiceText("Choose character to assign archery wound to - remaining wounds: " + woundsLeft);
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
