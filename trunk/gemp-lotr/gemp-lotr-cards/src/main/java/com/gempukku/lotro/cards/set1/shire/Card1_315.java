package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver or Skirmish: Heal a Hobbit.
 */
public class Card1_315 extends AbstractEvent {
    public Card1_315() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Stout and Sturdy", Phase.MANEUVER, Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndHealCharacterEffect(action, playerId, Filters.race(Race.HOBBIT)));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
