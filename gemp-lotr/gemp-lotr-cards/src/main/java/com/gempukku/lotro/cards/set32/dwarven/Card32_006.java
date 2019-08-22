package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;

import java.util.Collection;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Add a doubt to play a Dwarven follower from your draw deck.
 */
public class Card32_006 extends AbstractEvent {
    public Card32_006() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "King Under the Mountain", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddBurdenEffect(self.getOwner(), self, 1));
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, Culture.DWARVEN, CardType.FOLLOWER));
        return action;
    }
}
