package com.gempukku.lotro.cards.set5.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Add 3 burdens to wound a minion skirmishing a companion who has the Frodo signet twice.
 */
public class Card5_113 extends AbstractEvent {
    public Card5_113() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "No Use That Way", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddBurdenEffect(self, 3));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION, Filters.inSkirmishAgainst(CardType.COMPANION, Signet.FRODO)));
        return action;
    }
}
