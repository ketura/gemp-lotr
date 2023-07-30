package com.gempukku.lotro.processes.turn.tribbles;

import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.processes.DefaultGameProcess;
import com.gempukku.lotro.game.TribblesGame;
import com.gempukku.lotro.gamestate.tribbles.TribblesGameState;

public class TribblesStartOfRoundGameProcess extends DefaultGameProcess<TribblesGame> {

    private final String _firstPlayer;

    public TribblesStartOfRoundGameProcess(String firstPlayer) {
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process(TribblesGame game) {
        TribblesGameState gameState = game.getGameState();

        // Each new round begins with a new "chain" (starting with a card worth 1 Tribble) and play proceeds clockwise.
        gameState.setChainBroken(false);
        gameState.setNextTribble(1);
        gameState.getPlayerOrder().setReversed(false);

        // TODO: Handle "decked" players

        // Increment round number
        game.getGameState().advanceRoundNum();
        gameState.sendMessage("Beginning Round " + gameState.getRoundNum());

        // Draw new hands. Shuffle only on first round, since shuffling is already done at end of every round.
        for (String player : gameState.getPlayerOrder().getAllPlayers()) {
            if (gameState.getRoundNum() == 1) {
                gameState.shuffleDeck(player);
            }
            for (int i = 0; i < game.getFormat().getHandSize(); i++)
                gameState.playerDrawsCard(player);
        }

        gameState.setCurrentPlayerId(_firstPlayer);
    }

    @Override
    public GameProcess getNextProcess() {
        return new TribblesStartOfTurnGameProcess();
    }
}
