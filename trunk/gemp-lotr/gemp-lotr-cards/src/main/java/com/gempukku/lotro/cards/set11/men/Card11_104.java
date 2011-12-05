package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Event • Skirmish
 * Game Text: Wound a character skirmishing a [MEN] minion. If the fellowship is at a battleground site, you may remove
 * (3) to wound that character again.
 */
public class Card11_104 extends AbstractEvent {
    public Card11_104() {
        super(Side.SHADOW, 3, Culture.MEN, "Whistling Death", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.character, Filters.inSkirmishAgainst(Culture.MEN, CardType.MINION)) {
                    @Override
                    protected void woundedCardsCallback(Collection<PhysicalCard> cards) {
                        for (final PhysicalCard character : cards) {
                            action.appendEffect(
                                    new UnrespondableEffect() {
                                        @Override
                                        protected void doPlayEffect(final LotroGame game) {
                                            if (PlayConditions.location(game, Keyword.BATTLEGROUND)
                                                    && game.getGameState().getTwilightPool() >= 3) {
                                                action.appendEffect(
                                                        new PlayoutDecisionEffect(playerId,
                                                                new YesNoDecision("Do you wish to remove (3) to wound that character again?") {
                                                                    @Override
                                                                    protected void yes() {
                                                                        SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                                                                        subAction.appendCost(
                                                                                new RemoveTwilightEffect(3));
                                                                        subAction.appendEffect(
                                                                                new WoundCharactersEffect(self, character));
                                                                        game.getActionsEnvironment().addActionToStack(subAction);
                                                                    }
                                                                }));
                                            }
                                        }
                                    });
                        }
                    }
                });
        return action;
    }
}
