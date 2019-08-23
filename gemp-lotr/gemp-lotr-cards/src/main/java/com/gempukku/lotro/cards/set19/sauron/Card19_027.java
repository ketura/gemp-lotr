package com.gempukku.lotro.cards.set19.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Remove a Free Peoples culture token to add a threat. When you play this event, you may remove (1) to place
 * this on the bottom of your draw deck instead of in your discard pile.
 */
public class Card19_027 extends AbstractEvent {
    public Card19_027() {
        super(Side.SHADOW, 0, Culture.SAURON, "Sauron's Might", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canRemoveAnyCultureTokens(game, 1, Side.FREE_PEOPLE);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, null, 1, Side.FREE_PEOPLE));
        action.appendEffect(
                new AddThreatsEffect(playerId, self, 1));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        if (game.getGameState().getTwilightPool() >= 1) {
                            SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                            subAction.appendCost(
                                    new RemoveTwilightEffect(1));
                            subAction.appendEffect(
                                    new PutPlayedEventOnBottomOfDeckEffect(action));
                            action.appendEffect(
                                    new OptionalEffect(action, playerId,
                                            new StackActionEffect(subAction) {
                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Remove (1) to place this on the bottom of your draw deck";
                                                }
                                            }));
                        }
                    }
                });
        return action;
    }
}
