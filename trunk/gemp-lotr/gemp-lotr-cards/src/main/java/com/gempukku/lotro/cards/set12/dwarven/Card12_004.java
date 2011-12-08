package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make a Dwarf strength +2. Then, if that Dwarf is at a battleground or mountain site, draw a card.
 */
public class Card12_004 extends AbstractEvent {
    public Card12_004() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Durability", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.DWARF));
        if (PlayConditions.location(game, Filters.or(Keyword.BATTLEGROUND, Keyword.MOUNTAIN)))
            action.appendEffect(
                    new DrawCardsEffect(playerId, 1));
        return action;
    }
}
