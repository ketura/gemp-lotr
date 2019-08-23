package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.ShuffleCardsFromPlayAndStackedOnItIntoDeckEffect;

import java.util.Collection;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Regroup: Shuffle any number of [DWARVEN] conditions (and all cards stacked on them) into your draw deck.
 */
public class Card4_054 extends AbstractEvent {
    public Card4_054() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Rest by Blind Night", Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardsEffect(self, playerId, "Choose DWARVEN conditions", 0, Integer.MAX_VALUE, Culture.DWARVEN, CardType.CONDITION) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        action.insertEffect(
                                new ShuffleCardsFromPlayAndStackedOnItIntoDeckEffect(self, playerId, cards));
                    }
                });
        return action;
    }
}
