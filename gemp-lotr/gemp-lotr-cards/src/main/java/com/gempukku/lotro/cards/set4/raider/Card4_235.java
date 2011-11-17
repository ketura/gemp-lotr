package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Spot a [RAIDER] Man skirmishing a Ring-bound companion to add a burden.
 */
public class Card4_235 extends AbstractOldEvent {
    public Card4_235() {
        super(Side.SHADOW, Culture.RAIDER, "Gathering to the Summons", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.RAIDER, Filters.race(Race.MAN), Filters.inSkirmishAgainst(CardType.COMPANION, Filters.keyword(Keyword.RING_BOUND)));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddBurdenEffect(self, 1));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
