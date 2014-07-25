package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayerOrder;
import com.gempukku.lotro.logic.timing.PlayerOrderFeedback;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Arrays;
import java.util.Collections;

public class SetupSoloAdventureGameProcess implements GameProcess {
    private String _startingSite;
    private String _player;
    private PlayerOrderFeedback _playerOrderFeedback;
    private String _adventureCard;

    public SetupSoloAdventureGameProcess(String adventureCard, String startingSite, String player, PlayerOrderFeedback playerOrderFeedback) {
        _adventureCard = adventureCard;
        _startingSite = startingSite;
        _player = player;
        _playerOrderFeedback = playerOrderFeedback;
    }

    @Override
    public void process(LotroGame game) {
        _playerOrderFeedback.setPlayerOrder(new PlayerOrder(Arrays.asList(_player)), _player);
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
