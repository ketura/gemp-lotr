package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ShuffleDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Title: Trusted Advisor
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event - Fellowship
 * Card Number: 1R89
 * Game Text: Spot Gandalf and exert a companion of another culture to take a card from that companion's culture into hand from your draw deck.
 */
public class Card40_089 extends AbstractEvent {
    public Card40_089() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Trusted Advisor", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf)
                && PlayConditions.canExert(self, game, CardType.COMPANION, Filters.not(Culture.GANDALF));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION,
                        Filters.not(Culture.GANDALF)) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, character.getBlueprint().getCulture()));
                        action.appendEffect(
                                new ShuffleDeckEffect(playerId));
                    }
                });
        return action;
    }
}
