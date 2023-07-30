package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.actions.OptionalTriggerAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.effects.TransferPermanentEffect;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.game.PlayConditions;
import com.gempukku.lotro.game.TriggerConditions;

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
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult) {
                        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
                            final Filter followerTarget = Filters.and(Filters.owner(playerId), Filters.or(CardType.COMPANION, CardType.MINION));

                            List<OptionalTriggerAction> optionalTriggerActions = new LinkedList<>();
                            for (final LotroPhysicalCard follower : Filters.filterActive(game, CardType.FOLLOWER, Filters.owner(playerId))) {
                                if (follower.getBlueprint().canPayAidCost(game, follower)
                                        && PlayConditions.isActive(game, followerTarget)) {
                                    final OptionalTriggerAction action = new OptionalTriggerAction(follower);
                                    action.setText("Use " + GameUtils.getCardLink(follower) + " Aid");
                                    follower.getBlueprint().appendAidCosts(game, action, follower);
                                    action.appendCost(
                                            new ChooseActiveCardEffect(follower, playerId, "Choose character to transfer follower to", followerTarget, Filters.not(Filters.hasAttached(follower))) {
                                                @Override
                                                protected void cardSelected(DefaultGame game, LotroPhysicalCard card) {
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
