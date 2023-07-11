package com.gempukku.lotro.game.timing.processes.turn.lotro.regroup;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.SystemQueueAction;
import com.gempukku.lotro.game.timing.UnrespondableEffect;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.Collection;

public class ReturnFollowersToSupportGameProcess implements GameProcess {
    private final GameProcess _followingProcess;

    public ReturnFollowersToSupportGameProcess(GameProcess followingProcess) {
        _followingProcess = followingProcess;
    }

    @Override
    public void process(DefaultGame game) {
        SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        Collection<PhysicalCard> followers = Filters.filterActive(game, CardType.FOLLOWER, Zone.ATTACHED);
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
