package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Spot Aragorn to heal a companion who has the Aragorn signet twice.
 */
public class Card4_123 extends AbstractEvent {
    public Card4_123() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Hard Choice", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.aragorn);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndHealCharactersEffect(action, playerId, 1, 1, 2, CardType.COMPANION, Signet.ARAGORN));
        return action;
    }
}
