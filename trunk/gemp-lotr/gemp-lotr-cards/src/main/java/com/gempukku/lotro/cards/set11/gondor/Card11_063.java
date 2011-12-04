package com.gempukku.lotro.cards.set11.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Event • Fellowship
 * Game Text: Heal each [GONDOR] companion who has resistance 5 or more.
 */
public class Card11_063 extends AbstractEvent {
    public Card11_063() {
        super(Side.FREE_PEOPLE, 3, Culture.GONDOR, "Much-needed Rest", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new HealCharactersEffect(self, Culture.GONDOR, CardType.COMPANION, Filters.minResistance(5)));
        return action;
    }
}
