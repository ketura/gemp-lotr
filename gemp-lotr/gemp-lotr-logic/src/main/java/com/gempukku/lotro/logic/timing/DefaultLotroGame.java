package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.ActionsEnvironment;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.PlayerOrder;
import com.gempukku.lotro.logic.modifiers.ModifiersEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.apache.log4j.Logger;

import java.util.*;

public class DefaultLotroGame implements LotroGame {
    private static final Logger log = Logger.getLogger(DefaultLotroGame.class);

    private GameState _gameState;
    private ModifiersLogic _modifiersLogic = new ModifiersLogic();
    private DefaultActionsEnvironment _actionsEnvironment;
    private UserFeedback _userFeedback;
    private TurnProcedure _turnProcedure;
    private ActionStack _actionStack;

    private Map<String, GameStateListener> _gameStateListeners = new HashMap<String, GameStateListener>();
    private GameResultListener _gameResultListener;

    private Set<String> _allPlayers;

    private String _winnerPlayerId;
    private String _winReason;
    private Map<String, String> _losers = new HashMap<String, String>();

    public DefaultLotroGame(Map<String, LotroDeck> decks, UserFeedback userFeedback, GameResultListener gameResultListener, final LotroCardBlueprintLibrary library) {
        _gameResultListener = gameResultListener;
        _actionStack = new ActionStack();

        _allPlayers = decks.keySet();

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

        _gameState = new GameState();

        _turnProcedure = new TurnProcedure(this, decks.keySet(), userFeedback, _actionStack,
                new PlayerOrderFeedback() {
                    @Override
                    public void setPlayerOrder(PlayerOrder playerOrder, String firstPlayer) {
                        _gameState.init(playerOrder, firstPlayer, cards, library);
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
    public String getWinnerPlayerId() {
        return _winnerPlayerId;
    }

    @Override
    public void playerWon(String playerId, String reason) {
        _winnerPlayerId = playerId;
        _winReason = reason;
        if (_gameState != null)
            _gameState.sendMessage(_winnerPlayerId + " is the winner due to: " + reason);
        if (_gameResultListener != null) {
            Set<String> losers = new HashSet<String>(_allPlayers);
            losers.remove(_winnerPlayerId);
            _gameResultListener.gameFinished(_winnerPlayerId, losers, reason);
        }
    }

    @Override
    public void playerLost(String playerId, String reason) {
        if (_losers.get(playerId) == null) {
            log.debug("Player " + playerId + " lost due to: " + reason);
            _losers.put(playerId, reason);
            if (_gameState != null)
                _gameState.sendMessage(playerId + " lost due to: " + reason);

            if (_losers.size() + 1 == _allPlayers.size()) {
                List<String> allPlayers = new LinkedList<String>(_allPlayers);
                allPlayers.removeAll(_losers.keySet());
                playerWon(allPlayers.get(0), "Last remaining player in game");
            }
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

    @Override
    public void checkLoseConditions() {
        GameState gameState = getGameState();
        if (gameState != null && gameState.getCurrentPhase() != Phase.GAME_SETUP && gameState.getCurrentPhase() != Phase.BETWEEN_TURNS) {
            // Ring-bearer death
            if (!Filters.canSpot(gameState, getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER))) {
                playerLost(getGameState().getCurrentPlayerId(), "The Ring-Bearer is dead");
                return;
            } else {
                // Ring-bearer corruption
                PhysicalCard ringBearer = Filters.findFirstActive(getGameState(), getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER));
                int ringBearerResistance = getModifiersQuerying().getResistance(getGameState(), ringBearer);
                if (ringBearerResistance <= 0) {
                    playerLost(getGameState().getCurrentPlayerId(), "The Ring-Bearer is corrupted");
                    return;
                }
            }
        }
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

    public void removeGameStateListener(String playerId, GameStateListener gameStateListener) {
        if (_gameState != null)
            _gameState.removeGameStateListener(playerId, gameStateListener);
        else
            _gameStateListeners.remove(playerId);
    }
}
