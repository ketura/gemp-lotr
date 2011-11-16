package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.game.ActionsEnvironment;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
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

    private LotroFormat _format;

    private Set<String> _allPlayers;

    private String _winnerPlayerId;
    private Map<String, String> _losers = new HashMap<String, String>();

    private Set<GameResultListener> _gameResultListeners = new HashSet<GameResultListener>();

    public DefaultLotroGame(LotroFormat format, Map<String, LotroDeck> decks, UserFeedback userFeedback, final LotroCardBlueprintLibrary library) {
        _format = format;
        _actionStack = new ActionStack();

        _allPlayers = decks.keySet();

        _actionsEnvironment = new DefaultActionsEnvironment(this, _actionStack);

        final Map<String, List<String>> cards = new HashMap<String, List<String>>();
        final Map<String, String> ringBearers = new HashMap<String, String>();
        final Map<String, String> rings = new HashMap<String, String>();
        for (String playerId : decks.keySet()) {
            List<String> deck = new LinkedList<String>();

            LotroDeck lotroDeck = decks.get(playerId);
            deck.addAll(lotroDeck.getSites());
            deck.addAll(lotroDeck.getAdventureCards());

            cards.put(playerId, deck);
            ringBearers.put(playerId, lotroDeck.getRingBearer());
            rings.put(playerId, lotroDeck.getRing());
        }

        _gameState = new GameState();

        _turnProcedure = new TurnProcedure(this, decks.keySet(), userFeedback, _actionStack,
                new PlayerOrderFeedback() {
                    @Override
                    public void setPlayerOrder(PlayerOrder playerOrder, String firstPlayer) {
                        final GameStats gameStats = _turnProcedure.getGameStats();
                        _gameState.init(playerOrder, firstPlayer, cards, ringBearers, rings, library, gameStats);
                    }
                }
        );
        _userFeedback = userFeedback;

        RuleSet ruleSet = new RuleSet(this, _actionsEnvironment, _modifiersLogic);
        ruleSet.applyRuleSet();
    }

    public void addGameResultListener(GameResultListener listener) {
        _gameResultListeners.add(listener);
    }

    public void removeGameResultListener(GameResultListener listener) {
        _gameResultListeners.remove(listener);
    }

    @Override
    public LotroFormat getFormat() {
        return _format;
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
        // Any remaining players have lost
        Set<String> losers = new HashSet<String>(_allPlayers);
        losers.removeAll(_losers.keySet());
        losers.remove(playerId);

        for (String loser : losers)
            _losers.put(loser, "Other player won");

        gameWon(playerId, reason);
    }

    private void gameWon(String winner, String reason) {
        _winnerPlayerId = winner;

        if (_gameState != null)
            _gameState.sendMessage(_winnerPlayerId + " is the winner due to: " + reason);

        for (GameResultListener gameResultListener : _gameResultListeners)
            gameResultListener.gameFinished(_winnerPlayerId, reason, _losers);
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
                gameWon(allPlayers.get(0), "Last remaining player in game");
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
        if (gameState != null && gameState.getCurrentPhase() != Phase.PLAY_STARTING_FELLOWSHIP && gameState.getCurrentPhase() != Phase.BETWEEN_TURNS && gameState.getCurrentPhase() != Phase.PUT_RING_BEARER) {
            // Ring-bearer death
            PhysicalCard ringBearer = gameState.getRingBearer(gameState.getCurrentPlayerId());
            Zone zone = ringBearer.getZone();
            if (zone == null || !zone.isInPlay()) {
                playerLost(getGameState().getCurrentPlayerId(), "The Ring-Bearer is dead");
                return;
            } else {
                // Ring-bearer corruption
                int ringBearerResistance = getModifiersQuerying().getResistance(getGameState(), ringBearer);
                if (ringBearerResistance <= 0) {
                    playerLost(getGameState().getCurrentPlayerId(), "The Ring-Bearer is corrupted");
                    return;
                }
            }
        }
    }

    public void addGameStateListener(String playerId, GameStateListener gameStateListener) {
        _gameState.addGameStateListener(playerId, gameStateListener, _turnProcedure.getGameStats());
    }

    public void removeGameStateListener(GameStateListener gameStateListener) {
        _gameState.removeGameStateListener(gameStateListener);
    }
}
