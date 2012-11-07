package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MusterRule {
    private DefaultActionsEnvironment _actionsEnvironment;

    public MusterRule(DefaultActionsEnvironment actionsEnvironment) {
        _actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult) {
                        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                                && game.getGameState().getCurrentPhase() == Phase.REGROUP
                                && game.getGameState().getHand(playerId).size() > 0) {
                            PhysicalCard firstMuster = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Keyword.MUSTER);
                            if (firstMuster != null) {
                                final OptionalTriggerAction action = new OptionalTriggerAction(firstMuster);
                                action.setTriggerIdentifier("muster");
                                action.setVirtualCardAction(true);
                                action.setText("Use Muster");
                                ChooseAndDiscardCardsFromHandEffect effect = new ChooseAndDiscardCardsFromHandEffect(action, playerId, false,
                                        new ConstantEvaluator(0), new Evaluator() {
                                    @Override
                                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                        return Filters.filterActive(gameState, modifiersQuerying, Filters.owner(playerId), Keyword.MUSTER).size();
                                    }
                                }) {
                                    @Override
                                    protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                                        if (cardsBeingDiscarded.size() > 0)
                                            action.appendEffect(
                                                    new DrawCardsEffect(action, playerId, cardsBeingDiscarded.size()));
                                    }
                                };
                                effect.setText("Choose cards to discard to Muster (all at once)");
                                action.appendEffect(effect);

                                return Collections.singletonList(action);
                            }
                        }
                        return null;
                    }
                });
    }
}
