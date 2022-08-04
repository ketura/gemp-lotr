package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.LinkedList;
import java.util.List;

public class FollowerRule {
    private final DefaultActionsEnvironment defaultActionsEnvironment;

    public FollowerRule(DefaultActionsEnvironment defaultActionsEnvironment) {
        this.defaultActionsEnvironment = defaultActionsEnvironment;
    }

    public void applyRule() {
        defaultActionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
                            final Filter followerTarget = Filters.and(Filters.owner(playerId), Filters.or(CardType.COMPANION, CardType.MINION));

                            List<OptionalTriggerAction> optionalTriggerActions = new LinkedList<>();
                            for (final PhysicalCard follower : Filters.filterActive(game, CardType.FOLLOWER, Filters.owner(playerId))) {
                                if (follower.getBlueprint().canPayAidCost(game, follower)
                                        && PlayConditions.isActive(game, followerTarget)) {
                                    final OptionalTriggerAction action = new OptionalTriggerAction(follower);
                                    action.setText("Use " + GameUtils.getCardLink(follower) + " Aid");
                                    follower.getBlueprint().appendAidCosts(game, action, follower);
                                    action.appendCost(
                                            new ChooseActiveCardEffect(follower, playerId, "Choose character to transfer follower to", followerTarget, Filters.not(Filters.hasAttached(follower))) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                    action.appendEffect(
                                                            new TransferPermanentEffect(follower, card));
                                                }
                                            });
                                    optionalTriggerActions.add(action);
                                }
                            }
                            return optionalTriggerActions;
                        }
                        return null;
                    }
                });
    }
}
