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

import java.util.*;

public class DefaultLotroGame implements LotroGame {
    private GameState _gameState;
    private ModifiersLogic _modifiersLogic = new ModifiersLogic();
    private DefaultActionsEnvironment _actionsEnvironment;
    private UserFeedback _userFeedback;
    private TurnProcedure _turnProcedure;
    private ActionStack _actionStack;

    private Map<String, GameStateListener> _gameStateListeners = new HashMap<String, GameStateListener>();
    private GameResultListener _gameResultListener;

    public DefaultLotroGame(Map<String, LotroDeck> decks, UserFeedback userFeedback, GameResultListener gameResultListener, final LotroCardBlueprintLibrary library) {
        _gameResultListener = gameResultListener;
        _actionStack = new ActionStack();

        _actionsEnvironment = new DefaultActionsEnvironment(this, _actionStack);

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
    public void playerWon(String currentPlayerId) {
        _gameState.setWinnerPlayerId(currentPlayerId);
        if (_gameResultListener != null) {
            Set<String> losers = new HashSet<String>(_gameState.getPlayerOrder().getAllPlayers());
            losers.remove(currentPlayerId);
            _gameResultListener.gameFinished(currentPlayerId, losers);
        }
    }

    @Override
    public void playerLost(String currentPlayerId) {
        String winner = _gameState.setLoserPlayerId(currentPlayerId);
        if (winner != null && _gameResultListener != null) {
            Set<String> losers = new HashSet<String>(_gameState.getPlayerOrder().getAllPlayers());
            losers.remove(winner);
            _gameResultListener.gameFinished(winner, losers);
        }
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

    public ActionStack getActionStack() {
        return _actionStack;
    }

    public void addGameStateListener(String playerId, GameStateListener gameStateListener) {
        if (_gameState != null)
            _gameState.addGameStateListener(playerId, gameStateListener);
        else
            _gameStateListeners.put(playerId, gameStateListener);
    }
}
