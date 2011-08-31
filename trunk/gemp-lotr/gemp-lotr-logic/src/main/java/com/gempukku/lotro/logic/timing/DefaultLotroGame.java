package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.game.ActionsEnvironment;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.PlayerOrder;
import com.gempukku.lotro.logic.modifiers.ModifiersEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefaultLotroGame implements LotroGame {
    private GameState _gameState;
    private ModifiersLogic _modifiersLogic = new ModifiersLogic();
    private DefaultActionsEnvironment _actionsEnvironment;
    private UserFeedback _userFeedback;
    private TurnProcedure _turnProcedure;
    private ActionStack _actionStack;

    private Map<String, GameStateListener> _gameStateListeners = new HashMap<String, GameStateListener>();

    public DefaultLotroGame(Map<String, LotroDeck> decks, UserFeedback userFeedback, final LotroCardBlueprintLibrary library) {
        _actionStack = new ActionStack();

        _actionsEnvironment = new DefaultActionsEnvironment(this, decks.keySet(), _actionStack);

        final Map<String, List<String>> cards = new HashMap<String, List<String>>();
        for (String playerId : decks.keySet()) {
            List<String> deck = new LinkedList<String>();

            LotroDeck lotroDeck = decks.get(playerId);
            deck.add(lotroDeck.getRingBearer());
            deck.add(lotroDeck.getRing());
            deck.addAll(lotroDeck.getSites());
            deck.addAll(lotroDeck.getAdventureCards());

            cards.put(playerId, deck);
        }

        _turnProcedure = new TurnProcedure(this, decks.keySet(), userFeedback, _actionStack,
                new PlayerOrderFeedback() {
                    @Override
                    public void setPlayerOrder(PlayerOrder playerOrder, String firstPlayer) {
                        _gameState = new GameState(playerOrder, firstPlayer, cards, library);
                        for (Map.Entry<String, GameStateListener> stringGameStateListenerEntry : _gameStateListeners.entrySet())
                            _gameState.addGameStateListener(stringGameStateListenerEntry.getKey(), stringGameStateListenerEntry.getValue());
                        _gameStateListeners = null;

                    }
                }
        );
        _userFeedback = userFeedback;

        RuleSet ruleSet = new RuleSet(this, _actionsEnvironment, _modifiersLogic);
        ruleSet.applyRuleSet();
    }

    public void startGame() {
        _turnProcedure.carryOutPendingActionsUntilDecisionNeeded();
    }

    public void carryOutPendingActionsUntilDecisionNeeded() {
        _turnProcedure.carryOutPendingActionsUntilDecisionNeeded();
    }

    @Override
    public GameState getGameState() {
        return _gameState;
    }

    @Override
    public ActionsEnvironment getActionsEnvironment() {
        return _actionsEnvironment;
    }

    @Override
    public ModifiersEnvironment getModifiersEnvironment() {
        return _modifiersLogic;
    }

    @Override
    public ModifiersQuerying getModifiersQuerying() {
        return _modifiersLogic;
    }

    @Override
    public UserFeedback getUserFeedback() {
        return _userFeedback;
    }

    public void addGameStateListener(String playerId, GameStateListener gameStateListener) {
        if (_gameState != null)
            _gameState.addGameStateListener(playerId, gameStateListener);
        else
            _gameStateListeners.put(playerId, gameStateListener);
    }
}
