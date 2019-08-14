package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Spot 2 [ROHAN] Men (or 1 hunter [ROHAN] Man) to play a [ROHAN] companion or a [ROHAN] follower from your
 * draw deck.
 */
public class Card15_132 extends AbstractEvent {
    public Card15_132() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Last Days of My House", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return (PlayConditions.canSpot(game, 2, Culture.ROHAN, Race.MAN)
                || PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN, Keyword.HUNTER));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, Culture.ROHAN, Filters.or(CardType.COMPANION, CardType.FOLLOWER)));
        return action;
    }
}
