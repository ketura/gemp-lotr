package com.gempukku.lotro.processes.lotronly.skirmish;

import com.gempukku.lotro.actions.lotronly.SkirmishPhaseAction;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.gamestate.lotronly.Assignment;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.modifiers.ModifierFlag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayoutSkirmishesGameProcess implements GameProcess {
    private GameProcess _nextProcess;

    @Override
    public void process(DefaultGame game) {
        if (game.getModifiersQuerying().shouldSkipPhase(game, Phase.SKIRMISH, null)) {
            _nextProcess = new AfterSkirmishesGameProcess();
        } else {
            final GameState gameState = game.getGameState();
            final List<Assignment> assignments = gameState.getAssignments();

            if (assignments.size() > 0) {
                Set<LotroPhysicalCard> nonLurkerSkirmishFps = new HashSet<>();
                Set<LotroPhysicalCard> lurkerSkirmishFps = new HashSet<>();
                for (Assignment assignment : assignments) {
                    if (Filters.filter(assignment.getShadowCharacters(), game, Keyword.LURKER).size() > 0)
                        lurkerSkirmishFps.add(assignment.getFellowshipCharacter());
                    else
                        nonLurkerSkirmishFps.add(assignment.getFellowshipCharacter());
                }

                String playerChoosingSkirmishOrder = gameState.getCurrentPlayerId();
                if (game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.SKIRMISH_ORDER_BY_FIRST_SHADOW_PLAYER)) {
                    final PlayOrder playerOrder = gameState.getPlayerOrder().getCounterClockwisePlayOrder(playerChoosingSkirmishOrder, false);
                    // Skip first one
                    playerOrder.getNextPlayer();
                    playerChoosingSkirmishOrder = playerOrder.getNextPlayer();
                }

                SystemQueueAction chooseNextSkirmishAction = new SystemQueueAction();

                Set<LotroPhysicalCard> skirmishChoice;
                if (nonLurkerSkirmishFps.size() > 0)
                    skirmishChoice = nonLurkerSkirmishFps;
                else
                    skirmishChoice = lurkerSkirmishFps;

                ChooseActiveCardEffect chooseNextSkirmish = new ChooseActiveCardEffect(null, playerChoosingSkirmishOrder, "Choose next skirmish to resolve", Filters.in(skirmishChoice)) {
                    @Override
                    protected void cardSelected(DefaultGame game, LotroPhysicalCard card) {
                        game.getGameState().sendMessage("Next skirmish to resolve is for " + GameUtils.getCardLink(card));
                        final Assignment assignment = findAssignment(assignments, card);
                        game.getGameState().removeAssignment(assignment);
                        game.getActionsEnvironment().addActionToStack(
                                new SkirmishPhaseAction(assignment.getFellowshipCharacter(), assignment.getShadowCharacters()));
                    }
                };
                chooseNextSkirmish.setUseShortcut(false);

                chooseNextSkirmishAction.appendEffect(chooseNextSkirmish);

                game.getActionsEnvironment().addActionToStack(chooseNextSkirmishAction);
                _nextProcess = this;
            } else {
                _nextProcess = new AfterSkirmishesGameProcess();
            }
        }
    }

    private Assignment findAssignment(List<Assignment> assignments, LotroPhysicalCard freePeopleCharacter) {
        for (Assignment assignment : assignments) {
            if (assignment.getFellowshipCharacter() == freePeopleCharacter)
                return assignment;
        }
        return null;
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
