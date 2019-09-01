package com.gempukku.lotro.logic.timing.processes.turn.ai;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class AIPlayerAssignsArcheryTotalGameProcess implements GameProcess {
    private int _woundsToAssign;
    private GameProcess _followingProcess;

    public AIPlayerAssignsArcheryTotalGameProcess(int woundsToAssign, GameProcess followingProcess) {
        _woundsToAssign = woundsToAssign;
        _followingProcess = followingProcess;
    }

    @Override
    public void process(LotroGame game) {
        if (_woundsToAssign > 0) {
            final Filter filterPriority =
                    Filters.and(
                            CardType.MINION,
                            Filters.owner("AI"),
                            new Filter() {
                                @Override
                                public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                    return game.getModifiersQuerying().canTakeArcheryWound(game, physicalCard)
                                            && game.getGameState().getWounds(physicalCard) < game.getModifiersQuerying().getVitality(game, physicalCard) - 1;
                                }
                            });

            final Filter filterFallback =
                    Filters.and(
                            CardType.MINION,
                            Filters.owner("AI"),
                            new Filter() {
                                @Override
                                public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                    return game.getModifiersQuerying().canTakeArcheryWound(game, physicalCard);
                                }
                            }
                    );

            final SystemQueueAction action = new SystemQueueAction();
            for (int i = 0; i < _woundsToAssign; i++) {
                UnrespondableEffect chooseRandomMinionAndWound = new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        Collection<PhysicalCard> acceptableCards = Filters.filterActive(game, filterPriority);
                        if (acceptableCards.size() == 0)
                            acceptableCards = Filters.filterActive(game, filterFallback);

                        List<PhysicalCard> possibleChoices = new ArrayList<PhysicalCard>(acceptableCards);
                        if (possibleChoices.size()>0) {
                            SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                            Random rnd = new Random();
                            final int randomIndex = rnd.nextInt(possibleChoices.size());
                            WoundCharactersEffect woundCharacter = new WoundCharactersEffect((PhysicalCard) null, possibleChoices.get(randomIndex));
                            woundCharacter.setSourceText("Archery Fire");
                            subAction.appendEffect(woundCharacter);
                            game.getActionsEnvironment().addActionToStack(subAction);
                        }
                    }
                };
                action.appendEffect(chooseRandomMinionAndWound);
            }

            game.getActionsEnvironment().addActionToStack(action);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingProcess;
    }
}
