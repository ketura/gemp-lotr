package com.gempukku.lotro.game.timing.processes;

import com.gempukku.lotro.cards.CompletePhysicalCardVisitor;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.Action;

import java.util.LinkedList;
import java.util.List;

public class GatherPlayableActionsFromDiscardVisitor extends CompletePhysicalCardVisitor {
    private final DefaultGame _game;
    private final String _playerId;

    private final List<Action> _actions = new LinkedList<>();

    public GatherPlayableActionsFromDiscardVisitor(DefaultGame game, String playerId) {
        _game = game;
        _playerId = playerId;
    }

    @Override
    protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
        List<? extends Action> list = physicalCard.getBlueprint().getPhaseActionsFromDiscard(_playerId, _game, physicalCard);
        if (list != null) {
            for (Action action : list) {
                action.setVirtualCardAction(true);
                _actions.add(action);
            }
        }
    }

    public List<? extends Action> getActions() {
        return _actions;
    }
}
