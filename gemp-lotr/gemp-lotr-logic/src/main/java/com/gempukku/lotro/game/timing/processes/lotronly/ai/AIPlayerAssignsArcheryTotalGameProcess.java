package com.gempukku.lotro.game.timing.processes.lotronly.ai;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.UnrespondableEffect;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.game.effects.WoundCharactersEffect;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AIPlayerAssignsArcheryTotalGameProcess implements GameProcess {
    private final int _woundsToAssign;
    private final GameProcess _followingProcess;

    public AIPlayerAssignsArcheryTotalGameProcess(int woundsToAssign, GameProcess followingProcess) {
        _woundsToAssign = woundsToAssign;
        _followingProcess = followingProcess;
    }

    @Override
    public void process(DefaultGame game) {
        if (_woundsToAssign > 0) {
            final Filter filterPriority =
                    Filters.and(
                            CardType.MINION,
                            Filters.owner("AI"),
                            new Filter() {
                                @Override
                                public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
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
                                public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                                    return game.getModifiersQuerying().canTakeArcheryWound(game, physicalCard);
                                }
                            }
                    );

            final SystemQueueAction action = new SystemQueueAction();
            for (int i = 0; i < _woundsToAssign; i++) {
                UnrespondableEffect chooseRandomMinionAndWound = new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        Collection<LotroPhysicalCard> acceptableCards = Filters.filterActive(game, filterPriority);
                        if (acceptableCards.size() == 0)
                            acceptableCards = Filters.filterActive(game, filterFallback);

                        List<LotroPhysicalCard> possibleChoices = new ArrayList<>(acceptableCards);
                        if (possibleChoices.size()>0) {
                            SubAction subAction = new SubAction(action);
                            final int randomIndex = ThreadLocalRandom.current().nextInt(possibleChoices.size());
                            WoundCharactersEffect woundCharacter = new WoundCharactersEffect((LotroPhysicalCard) null, possibleChoices.get(randomIndex));
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
