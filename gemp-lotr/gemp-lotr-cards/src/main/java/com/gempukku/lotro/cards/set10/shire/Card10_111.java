package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: Spot 2 Hobbit companions to make a Shadow player discard a minion (or spot 4 Hobbit companions to make
 * that player discard 2 minions).
 */
public class Card10_111 extends AbstractEvent {
    public Card10_111() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Narrow Escape", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Race.HOBBIT, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        final int discardCount = (Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Race.HOBBIT, CardType.COMPANION) >= 4) ? 2 : 1;
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        action.appendEffect(
                                new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, discardCount, discardCount, CardType.MINION));
                    }
                });
        return action;
    }
}
