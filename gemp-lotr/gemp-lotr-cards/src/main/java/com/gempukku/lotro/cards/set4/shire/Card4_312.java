package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Spot Frodo and add a burden to wound a minion skirmishing a companion who has the Frodo signet.
 */
public class Card4_312 extends AbstractEvent {
    public Card4_312() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Mind Your Own Affairs", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.frodo);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddBurdenEffect(self.getOwner(), self, 1));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.inSkirmishAgainst(CardType.COMPANION, Signet.FRODO)));
        return action;
    }
}
