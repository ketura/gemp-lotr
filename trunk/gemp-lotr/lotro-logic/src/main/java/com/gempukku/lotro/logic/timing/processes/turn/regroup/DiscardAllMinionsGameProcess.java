package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.EndOfTurnGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;

import java.util.List;

public class DiscardAllMinionsGameProcess implements GameProcess {
    private LotroGame _game;

    public DiscardAllMinionsGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();
        List<PhysicalCard> minions = Filters.filterActive(gameState, _game.getModifiersQuerying(), Filters.type(CardType.MINION));
        for (PhysicalCard minion : minions) {
            gameState.removeCardFromZone(minion);
            gameState.addCardToZone(minion, Zone.DISCARD);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return new EndOfPhaseGameProcess(_game, Phase.REGROUP,
                new EndOfTurnGameProcess(_game));
    }
}
