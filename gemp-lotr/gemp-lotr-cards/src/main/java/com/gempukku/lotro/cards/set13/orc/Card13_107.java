package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: If you can spot more [ORC] conditions than [ORC] minions, you may discard your [ORC] condition from play
 * to play an [ORC] minion from your discard pile.
 */
public class Card13_107 extends AbstractEvent {
    public Card13_107() {
        super(Side.SHADOW, 0, Culture.ORC, "Expendable Servants", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && (
                Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Culture.ORC, CardType.CONDITION)
                        > Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Culture.ORC, CardType.MINION))
                && PlayConditions.canDiscardFromPlay(self, game, Filters.owner(playerId), Culture.ORC, CardType.CONDITION)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ORC, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.owner(playerId), Culture.ORC, CardType.CONDITION));
        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ORC, CardType.MINION));
        return action;
    }
}
