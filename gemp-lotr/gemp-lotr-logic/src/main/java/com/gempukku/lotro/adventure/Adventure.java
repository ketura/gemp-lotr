package com.gempukku.lotro.adventure;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.modifiers.ModifiersLogic;
import com.gempukku.lotro.game.PlayerOrderFeedback;
import com.gempukku.lotro.processes.GameProcess;

import java.util.Set;

public interface Adventure {
    public void applyAdventureRules(DefaultGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic);

    public GameProcess getStartingGameProcess(Set<String> players, PlayerOrderFeedback playerOrderFeedback);

    public GameProcess getAfterFellowshipPhaseGameProcess();

    public void appendNextSiteAction(SystemQueueAction action);

    public GameProcess getAfterFellowshipArcheryGameProcess(int fellowshipArcheryTotal, GameProcess followingProcess);

    public GameProcess getAfterFellowshipAssignmentGameProcess(Set<LotroPhysicalCard> leftoverMinions, GameProcess followingProcess);

    public GameProcess getBeforeFellowshipChooseToMoveGameProcess(GameProcess followingProcess);

    public GameProcess getPlayerStaysGameProcess(DefaultGame game, GameProcess followingProcess);

    public boolean isSolo();
}
