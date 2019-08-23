package com.gempukku.lotro.cards.set20.shire;

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
 * 0
 * Crossing the Threshold
 * Event • Skirmish
 * Put on The One Ring to wound each minion skirmishing the Ring-bearer.
 * http://lotrtcg.org/coreset/shire/crossingthethreshold(r2).jpg
 */
public class Card20_479 extends AbstractEvent {
    public Card20_479() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Crossing the Threshold", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return !game.getGameState().isWearingRing();
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new PutOnTheOneRingEffect());
        action.appendEffect(
                new WoundCharactersEffect(self, CardType.MINION, Filters.inSkirmishAgainst(Filters.ringBearer)));
        return action;
    }
}
