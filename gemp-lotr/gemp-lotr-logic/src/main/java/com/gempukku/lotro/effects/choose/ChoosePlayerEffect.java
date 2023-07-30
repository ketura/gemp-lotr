package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.effects.UnrespondableEffect;
import com.gempukku.lotro.rules.GameUtils;

public abstract class ChoosePlayerEffect extends UnrespondableEffect {
    private final String _playerId;

    public ChoosePlayerEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        String[] players = GameUtils.getAllPlayers(game);
        if (players.length == 1)
            playerChosen(players[0]);
        else
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose a player", players) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            playerChosen(result);
                        }
                    });
    }

    protected abstract void playerChosen(String playerId);
}
