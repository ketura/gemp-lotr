package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

/**
 * Title: Crossing the Threshold
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1R245
 * Game Text: Put on The One Ring to wound each minion skirmishing the Ring-bearer.
 */
public class Card40_245 extends AbstractEvent {
    public Card40_245() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Crossing the Threshold", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return !game.getGameState().isWearingRing();
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new PutOnTheOneRingEffect());
        action.appendEffect(
                new WoundCharactersEffect(self, CardType.MINION, Filters.inSkirmishAgainst(Filters.ringBearer)));
        return action;
    }
}
