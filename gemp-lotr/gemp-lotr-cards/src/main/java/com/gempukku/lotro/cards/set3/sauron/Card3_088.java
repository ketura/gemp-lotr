package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Shadow: Spot a [SAURON] minion and a Nazgul to make the Free Peoples player discard a card from
 * the top of his or her deck for each burden you can spot.
 */
public class Card3_088 extends AbstractOldEvent {
    public Card3_088() {
        super(Side.SHADOW, Culture.SAURON, "Get Off the Road!", Phase.SHADOW);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), CardType.MINION)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        int burdens = game.getGameState().getBurdens();
        for (int i = 0; i < burdens; i++)
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, playerId, true));
        return action;
    }
}
