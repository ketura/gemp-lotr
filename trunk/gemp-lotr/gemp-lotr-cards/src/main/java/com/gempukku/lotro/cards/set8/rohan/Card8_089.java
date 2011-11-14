package com.gempukku.lotro.cards.set8.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Exert a minion skirmishing a mounted [ROHAN] Man.
 */
public class Card8_089 extends AbstractEvent {
    public Card8_089() {
        super(Side.FREE_PEOPLE, 2, Culture.ROHAN, "Fury of the Northmen", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(Culture.ROHAN, Race.MAN, Filters.mounted)));
        return action;
    }
}
