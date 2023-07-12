package com.gempukku.lotro.game.adventure;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;
import com.gempukku.lotro.game.timing.PlayerOrderFeedback;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.Set;

public interface Adventure {
    public void applyAdventureRules(DefaultGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic);

    public GameProcess getStartingGameProcess(Set<String> players, PlayerOrderFeedback playerOrderFeedback);

    public GameProcess getAfterFellowshipPhaseGameProcess();

    public void appendNextSiteAction(SystemQueueAction action);

    public GameProcess getAfterFellowshipArcheryGameProcess(int fellowshipArcheryTotal, GameProcess followingProcess);

    public GameProcess getAfterFellowshipAssignmentGameProcess(Set<PhysicalCard> leftoverMinions, GameProcess followingProcess);

    public GameProcess getBeforeFellowshipChooseToMoveGameProcess(GameProcess followingProcess);

    public GameProcess getPlayerStaysGameProcess(DefaultGame game, GameProcess followingProcess);

    public boolean isSolo();
}
