package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Add a burden to heal a Ring-bound companion twice.
 */
public class Card4_309 extends AbstractEvent {
    public Card4_309() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Light Shining Faintly", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddBurdenEffect(self, 1));
        action.appendEffect(
                new ChooseAndHealCharactersEffect(action, playerId, 1, 1, 2, CardType.COMPANION, Keyword.RING_BOUND));
        return action;
    }
}
