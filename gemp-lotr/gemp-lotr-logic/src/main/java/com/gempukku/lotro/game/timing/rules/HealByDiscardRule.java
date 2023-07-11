package com.gempukku.lotro.game.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.actions.ActivateCardAction;
import com.gempukku.lotro.game.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.game.effects.HealCharactersEffect;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HealByDiscardRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public HealByDiscardRule(DefaultActionsEnvironment actionsEnvironment) {
        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getPhaseActions(String playerId, DefaultGame game) {
                        if (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP
                                && GameUtils.isFP(game, playerId)) {
                            List<Action> result = new LinkedList<>();
                            for (PhysicalCard card : Filters.filter(game.getGameState().getHand(playerId), game, Filters.or(CardType.COMPANION, CardType.ALLY), Filters.unique)) {
                                PhysicalCard active = Filters.findFirstActive(game, Filters.name(card.getBlueprint().getTitle()));
                                if (active != null && game.getGameState().getWounds(active) > 0) {
                                    ActivateCardAction action = new ActivateCardAction(card);
                                    action.setText("Heal by discarding");
                                    action.appendCost(new DiscardCardsFromHandEffect(null, card.getOwner(), Collections.singleton(card), false));
                                    action.appendCost(
                                            new UnrespondableEffect() {
                                                @Override
                                                protected void doPlayEffect(DefaultGame game) {
                                                    game.getGameState().eventPlayed(card);
                                                }
                                            });

                                    action.appendEffect(new HealCharactersEffect(card, card.getOwner(), active));
                                    result.add(action);
                                }
                            }
                            return result;
                        }
                        return null;
                    }
                });
    }
}
