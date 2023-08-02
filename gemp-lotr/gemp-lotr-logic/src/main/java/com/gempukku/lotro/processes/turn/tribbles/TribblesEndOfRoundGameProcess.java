package com.gempukku.lotro.processes.turn.tribbles;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.TribblesGame;
import com.gempukku.lotro.gamestate.TribblesGameState;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.processes.DefaultGameProcess;

import java.util.*;

public class TribblesEndOfRoundGameProcess extends DefaultGameProcess<TribblesGame> {
    private final Map<String, Integer> _pointsScored = new HashMap<>();
    private GameProcess _nextProcess;
    @Override
    public void process(TribblesGame game) {
        
        TribblesGameState gameState = game.getGameState();

        for (String playerId : game.getPlayers()) {

            // Count the total number of Tribbles in the play piles of the players who "went out" and score points.
            if (gameState.getHand(playerId).size() == 0) {
                gameState.playerWentOut(playerId); // TODO: Not implemented yet
                int score = calculateScore(gameState.getPlayPile(playerId));
                _pointsScored.put(playerId, score);
                gameState.playerScored(playerId, score);
                gameState.sendMessage(playerId + " went out with " + score + " points");
            }

            // Each player places the cards remaining in their hand into their discard pile.
            gameState.discardHand(game, playerId);

            // Each player then shuffles their play pile into their decks.
            gameState.shufflePlayPileIntoDeck(game, playerId);
        }

        if (gameState.isLastRound()) {
            // TODO: Game is over. Determine results.
        } else {
            /* The player who "went out" this round will take the first turn in the next round.
                If multiple players "went out" in the previous round, the player who "went out" with the
                lowest points scored will play first. */
            int lowestScore = Collections.min(_pointsScored.values());
            _pointsScored.entrySet().removeIf(entry -> entry.getValue() > lowestScore);
            List<String> firstPlayerList = new ArrayList<>(_pointsScored.keySet());

                /* In most games, there will be a clear first player at this point. For cases where multiple
                    players went out with the same lowest score, this code will randomly select one to be the
                    first player.
                 */
            String firstPlayer = firstPlayerList.get(new Random().nextInt(firstPlayerList.size()));
            gameState.sendMessage("DEBUG: " + firstPlayer + " will go first next round.");
            _nextProcess = new TribblesStartOfRoundGameProcess(firstPlayer);
        }
    }

    public int calculateScore(List<LotroPhysicalCard> playPile) {
        int score = 0;
        for (LotroPhysicalCard card : playPile) {
            score += card.getBlueprint().getTribbleValue();
        }
        return score;
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}