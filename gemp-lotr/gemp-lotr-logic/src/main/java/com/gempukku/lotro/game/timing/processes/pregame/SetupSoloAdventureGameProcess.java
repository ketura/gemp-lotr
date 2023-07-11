package com.gempukku.lotro.game.timing.processes.pregame;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.timing.PlayerOrder;
import com.gempukku.lotro.game.timing.PlayerOrderFeedback;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.Collections;

public class SetupSoloAdventureGameProcess implements GameProcess {
    private final String _startingSite;
    private final String _player;
    private final PlayerOrderFeedback _playerOrderFeedback;
    private final String _adventureCard;

    public SetupSoloAdventureGameProcess(String adventureCard, String startingSite, String player, PlayerOrderFeedback playerOrderFeedback) {
        _adventureCard = adventureCard;
        _startingSite = startingSite;
        _player = player;
        _playerOrderFeedback = playerOrderFeedback;
    }

    @Override
    public void process(DefaultGame game) {
        _playerOrderFeedback.setPlayerOrder(new PlayerOrder(Collections.singletonList(_player)), _player);
        final GameState gameState = game.getGameState();
        try {
            final PhysicalCard adventureCard = gameState.createPhysicalCard(_player, game.getLotroCardBlueprintLibrary(), _adventureCard);
            gameState.addCardToZone(game, adventureCard, Zone.SUPPORT);

            final PhysicalCard startingSite = gameState.createPhysicalCard("AI", game.getLotroCardBlueprintLibrary(), _startingSite);
            startingSite.setSiteNumber(1);
            gameState.addCardToZone(game, startingSite, Zone.ADVENTURE_PATH);

            game.getGameState().setPlayerPosition(_player, 1);
        } catch (CardNotFoundException exp) {
            // Ignore
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return new PlayRingBearerRingAndAddBurdersGameProcess(Collections.singletonMap(_player, 0), _player);
    }
}
