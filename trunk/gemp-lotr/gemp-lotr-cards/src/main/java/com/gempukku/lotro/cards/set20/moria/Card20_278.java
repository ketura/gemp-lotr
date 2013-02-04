package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

/**
 * 0
 * Power and Terror
 * Moria	Event • Shadow
 * Reveal any number of [Moria] Goblins from your hand to play the Balrog.
 * It's twilight cost is -2 for each Goblin revealed.
 */
public class Card20_278 extends AbstractEvent {
    public Card20_278() {
        super(Side.SHADOW, 0, Culture.MORIA, "Power and Terror", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.balrog,
                Filters.playable(game, -2 * Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.MORIA, Race.GOBLIN).size())).size() > 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseCardsFromHandEffect(playerId, 0, Integer.MAX_VALUE, Culture.MORIA, Race.GOBLIN) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                        action.appendEffect(
                                new RevealCardEffect(self, selectedCards));
                        int cardsRevealed = selectedCards.size();
                        action.appendEffect(
                                new ChooseAndPlayCardFromHandEffect(playerId, game, -2 * cardsRevealed, Filters.balrog));
                    }
                });
        return action;
    }
}
