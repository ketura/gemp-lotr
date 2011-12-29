package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Collection;

public class ReturnFollowersToSupportGameProcess implements GameProcess {
    private GameProcess _followingProcess;

    public ReturnFollowersToSupportGameProcess(GameProcess followingProcess) {
        _followingProcess = followingProcess;
    }

    @Override
    public void process(LotroGame game) {
        SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        Collection<PhysicalCard> followers = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), CardType.FOLLOWER, Zone.ATTACHED);
                        game.getGameState().removeCardsFromZone(game.getGameState().getCurrentPlayerId(), followers);
                        for (PhysicalCard attachedFollowers : followers)
                            game.getGameState().addCardToZone(game, attachedFollowers, Zone.SUPPORT);
                    }
                });
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingProcess;
    }
}
