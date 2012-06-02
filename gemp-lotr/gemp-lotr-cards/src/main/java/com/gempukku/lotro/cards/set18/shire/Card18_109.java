package com.gempukku.lotro.cards.set18.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Remove 2 [SHIRE] tokens to cancel a skirmish involving an unbound Hobbit.
 */
public class Card18_109 extends AbstractEvent {
    public Card18_109() {
        super(Side.FREE_PEOPLE, 2, Culture.SHIRE, "Make a Run For It", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canRemoveTokensFromAnything(game, Token.SHIRE, 2);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.SHIRE, 1, Filters.any));
        action.appendCost(
                new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.SHIRE, 1, Filters.any));
        action.appendEffect(
                new CancelSkirmishEffect(Filters.unboundCompanion, Race.HOBBIT));
        return action;
    }
}
