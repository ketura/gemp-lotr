package com.gempukku.lotro.cards.set8.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Spot Gandalf and exert a companion to place a card of that companion’s culture from your discard pile on
 * top of your draw deck.
 */
public class Card8_019 extends AbstractEvent {
    public Card8_019() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "On Your Doorstep", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf)
                && PlayConditions.canExert(self, game, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        Culture culture = character.getBlueprint().getCulture();
                        action.appendEffect(
                                new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 1, 1, culture));
                    }
                });
        return action;
    }
}
