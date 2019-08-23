package com.gempukku.lotro.cards.set18.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canRemoveTokensFromAnything(game, Token.SHIRE, 2);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
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
