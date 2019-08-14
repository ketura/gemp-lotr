package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RevealCardsFromYourHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;

import java.util.Collection;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Reveal any number of [MORIA] Orcs from your hand to play The Balrog. Its twilight cost is -2 for
 * each Orc revealed.
 */
public class Card2_070 extends AbstractEvent {
    public Card2_070() {
        super(Side.SHADOW, 0, Culture.MORIA, "Power and Terror", Phase.SHADOW);
    }


    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.filter(game.getGameState().getHand(self.getOwner()), game, Filters.balrog,
                Filters.playable(game, -2 * Filters.filter(game.getGameState().getHand(self.getOwner()), game, Culture.MORIA, Race.ORC).size())).size() > 0;
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseCardsFromHandEffect(playerId, 0, Integer.MAX_VALUE, Culture.MORIA, Race.ORC) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                        action.appendEffect(
                                new RevealCardsFromYourHandEffect(self, playerId, selectedCards));
                        int cardsRevealed = selectedCards.size();
                        action.appendEffect(
                                new ChooseAndPlayCardFromHandEffect(playerId, game, -2 * cardsRevealed, Filters.balrog));
                    }
                });
        return action;
    }
}
