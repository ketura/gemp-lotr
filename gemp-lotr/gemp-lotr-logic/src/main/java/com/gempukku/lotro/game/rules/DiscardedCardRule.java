package com.gempukku.lotro.game.rules;

import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.timing.results.DiscardCardsFromPlayResult;
import com.gempukku.lotro.game.actions.OptionalTriggerAction;
import com.gempukku.lotro.game.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.Collections;
import java.util.List;

public class DiscardedCardRule {
    private final DefaultActionsEnvironment _actionsEnvironment;

    public DiscardedCardRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY) {
                            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
                            final PhysicalCard discardedCard = discardResult.getDiscardedCard();
                            RequiredTriggerAction trigger = discardedCard.getBlueprint().getDiscardedFromPlayRequiredTrigger(game, discardedCard);
                            if (trigger != null)
                                return Collections.singletonList(trigger);
                        }
                        return null;
                    }

                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY) {
                            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
                            final PhysicalCard discardedCard = discardResult.getDiscardedCard();
                            if (discardedCard.getOwner().equals(playerId)) {
                                OptionalTriggerAction trigger = discardedCard.getBlueprint().getDiscardedFromPlayOptionalTrigger(playerId, game, discardedCard);
                                if (trigger != null) {
                                    trigger.setVirtualCardAction(true);
                                    return Collections.singletonList(trigger);
                                }
                            }
                        }
                        return null;
                    }
                });
    }
}
