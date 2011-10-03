package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Collection;

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
            final GameState gameState = _game.getGameState();
            Collection<PhysicalCard> possibleWoundTargets = Filters.filterActive(gameState, _game.getModifiersQuerying(),
                    Filters.or(
                            Filters.type(CardType.COMPANION),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    return modifiersQuerying.isAllyOnCurrentSite(gameState, physicalCard);
                                }
                            }));

            if (possibleWoundTargets.size() > 0) {
                if (possibleWoundTargets.size() == 1) {
                    PhysicalCard selectedCard = possibleWoundTargets.iterator().next();
                    RequiredTriggerAction action = new RequiredTriggerAction(null);
                    action.appendEffect(new WoundCharacterEffect((PhysicalCard) null, selectedCard));
                    _game.getActionsEnvironment().addActionToStack(action);
                    if (_woundsToAssign > 1)
                        _nextProcess = new FellowshipPlayerAssignsArcheryDamageGameProcess(_game, _woundsToAssign - 1, _followingGameProcess);
                    else
                        _nextProcess = _followingGameProcess;
                } else {
                    _game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                            new CardsSelectionDecision(1, "Choose companion or ally at home to assign archery wound to - remaining wounds: " + _woundsToAssign, possibleWoundTargets, 1, 1) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    PhysicalCard selectedCard = getSelectedCardsByResponse(result).iterator().next();
                                    RequiredTriggerAction action = new RequiredTriggerAction(null);
                                    action.appendEffect(new WoundCharacterEffect((PhysicalCard) null, selectedCard));
                                    _game.getActionsEnvironment().addActionToStack(action);
                                    if (_woundsToAssign > 1)
                                        _nextProcess = new FellowshipPlayerAssignsArcheryDamageGameProcess(_game, _woundsToAssign - 1, _followingGameProcess);
                                    else
                                        _nextProcess = _followingGameProcess;
                                }
                            });
                }
            } else {
                _nextProcess = _followingGameProcess;
            }
        } else {
            _nextProcess = _followingGameProcess;
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
