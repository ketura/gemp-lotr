package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Spot a [RAIDER] Man skirmishing a Ring-bound companion to add a burden.
 */
public class Card4_235 extends AbstractEvent {
    public Card4_235() {
        super(Side.SHADOW, 0, Culture.RAIDER, "Gathering to the Summons", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.RAIDER, Race.MAN, Filters.inSkirmishAgainst(CardType.COMPANION, Keyword.RING_BOUND));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddBurdenEffect(self.getOwner(), self, 1));
        return action;
    }
}
