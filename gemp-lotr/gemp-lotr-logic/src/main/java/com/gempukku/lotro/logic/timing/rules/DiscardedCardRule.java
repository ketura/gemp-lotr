package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DiscardedCardRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public DiscardedCardRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.DISCARD_FROM_PLAY) {
                            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
                            Collection<PhysicalCard> discardedCards = discardResult.getDiscardedCards();
                            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
                            for (PhysicalCard discardedCard : discardedCards) {
                                RequiredTriggerAction trigger = discardedCard.getBlueprint().getDiscardedFromPlayTrigger(game, discardedCard);
                                if (trigger != null)
                                    actions.add(trigger);
                            }
                            return actions;
                        }
                        return null;
                    }
                });
    }
}
