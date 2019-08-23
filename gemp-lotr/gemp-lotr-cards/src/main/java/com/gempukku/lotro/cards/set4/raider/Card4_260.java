package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Skirmish: Wound a companion or ally skirmishing a [RAIDER] Man.
 */
public class Card4_260 extends AbstractEvent {
    public Card4_260() {
        super(Side.SHADOW, 3, Culture.RAIDER, "Whirling Strike", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.or(CardType.COMPANION, CardType.ALLY),
                        Filters.inSkirmishAgainst(Culture.RAIDER, Race.MAN)));
        return action;
    }
}
