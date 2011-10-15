package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Stealth. Regroup: Spot 2 Hobbits to make each site's Shadow number -2 until the end of the turn.
 */
public class Card2_099 extends AbstractOldEvent {
    public Card2_099() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Deft in Their Movements", Phase.REGROUP);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT)) >= 2;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfTurnModifierEffect(
                        new TwilightCostModifier(self, Filters.type(CardType.SITE), -2)));
        return action;
    }
}
