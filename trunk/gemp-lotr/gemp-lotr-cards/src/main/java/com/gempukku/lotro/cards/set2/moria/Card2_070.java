package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.cards.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
        super(Side.SHADOW, Culture.MORIA, "Power and Terror", Phase.SHADOW);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.name("The Balrog"),
                Filters.playable(game, -2 * Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.race(Race.ORC)).size())).size() > 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseCardsFromHandEffect(playerId, 0, Integer.MAX_VALUE, Filters.culture(Culture.MORIA), Filters.race(Race.ORC)) {
                    @Override
                    protected void cardsSelected(Collection<PhysicalCard> selectedCards) {
                        int cardsRevealed = selectedCards.size();
                        action.appendEffect(
                                new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Filters.name("The Balrog"), -2 * cardsRevealed));
                    }
                });
        return action;
    }
}
