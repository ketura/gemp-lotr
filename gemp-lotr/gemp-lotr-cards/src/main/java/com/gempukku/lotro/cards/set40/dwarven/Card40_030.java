package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Title: Take Cover!
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event - Archery
 * Card Number: 1U30
 * Game Text: Discard a [DWARVEN] condition to make the minion archery total -X, where X is the number of cards that were stacked on that condition.
 */
public class Card40_030 extends AbstractEvent{
    public Card40_030() {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "Take Cover!", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardFromPlay(self, game, Culture.DWARVEN, CardType.CONDITION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.CONDITION) {
                    @Override
                    protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {
                        int total = game.getGameState().getStackedCards(cards.iterator().next()).size();
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new ArcheryTotalModifier(self, Side.SHADOW, -total)));
                    }
                });
        return action;
    }
}
