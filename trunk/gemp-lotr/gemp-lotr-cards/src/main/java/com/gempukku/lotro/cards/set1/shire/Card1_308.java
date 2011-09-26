package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Add 1 burden to wound each minion skirmishing the Ring-bearer.
 */
public class Card1_308 extends AbstractEvent {
    public Card1_308() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Power According to His Stature", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(new AddBurdenEffect(playerId));
        Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish != null) {
            if (game.getModifiersQuerying().hasKeyword(game.getGameState(), skirmish.getFellowshipCharacter(), Keyword.RING_BEARER)) {
                action.appendEffect(
                        new WoundCharacterEffect(playerId, Filters.and(Filters.type(CardType.MINION), Filters.inSkirmish())));
            }
        }
        return action;
    }
}
