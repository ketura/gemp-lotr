package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: Heal a Hobbit for each minion you spot.
 */
public class Card11_176 extends AbstractEvent {
    public Card11_176() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Unharmed", Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        int count = Filters.countActive(game, CardType.MINION);
        for (int i = 0; i < count; i++)
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 0, 1, Race.HOBBIT));
        return action;
    }
}
