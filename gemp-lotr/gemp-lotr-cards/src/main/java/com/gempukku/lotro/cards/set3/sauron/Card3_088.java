package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Shadow: Spot a [SAURON] minion and a Nazgul to make the Free Peoples player discard a card from
 * the top of his or her deck for each burden you can spot.
 */
public class Card3_088 extends AbstractEvent {
    public Card3_088() {
        super(Side.SHADOW, 0, Culture.SAURON, "Get Off the Road!", Phase.SHADOW);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.SAURON, CardType.MINION)
                && Filters.canSpot(game, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        int burdens = game.getGameState().getBurdens();
        if (burdens > 0)
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, game.getGameState().getCurrentPlayerId(), burdens, true));
        return action;
    }
}
