package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.filters.Filters;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Event • Fellowship
 * Game Text: Spot an Elf to heal each companion who has resistance 5 or more.
 */
public class Card11_019 extends AbstractEvent {
    public Card11_019() {
        super(Side.FREE_PEOPLE, 3, Culture.ELVEN, "Farewell to Lorien", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action= new PlayEventAction(self);
        action.appendEffect(
                new HealCharactersEffect(self, CardType.COMPANION, Filters.minResistance(5)));
        return action;
    }
}
