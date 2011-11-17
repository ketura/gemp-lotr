package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Stack the top 2 cards from your draw deck on a [DWARVEN] condition that has a card already stacked on it.
 */
public class Card4_043 extends AbstractOldEvent {
    public Card4_043() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Come Here Lad", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose DWARVEN condition", Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Filters.any)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new StackTopCardsFromDeckEffect(self, playerId, 2, card));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
