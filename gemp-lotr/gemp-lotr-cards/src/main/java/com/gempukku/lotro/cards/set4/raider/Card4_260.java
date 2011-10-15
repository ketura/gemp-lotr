package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Skirmish: Wound a companion or ally skirmishing a [RAIDER] Man.
 */
public class Card4_260 extends AbstractOldEvent {
    public Card4_260() {
        super(Side.SHADOW, Culture.RAIDER, "Whirling Strike", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)),
                        Filters.inSkirmishAgainst(Filters.culture(Culture.RAIDER), Filters.race(Race.MAN))));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }
}
