package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;

import java.util.Collection;

/**
 * 2
 * Take Cover!
 * Dwarven	Event • Archery
 * Discard X cards stacked on a [Dwarven] condition to make the minion archery total -X.
 */
public class Card20_070 extends AbstractEvent {
    public Card20_070() {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "Take Cover!", Phase.ARCHERY);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose DWARVEN condition to discard cards from", Culture.DWARVEN, CardType.CONDITION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new ChooseAndDiscardStackedCardsEffect(action, playerId, 0, Integer.MAX_VALUE, card, Filters.any) {
                                    @Override
                                    protected void discardingCardsCallback(Collection<PhysicalCard> cards) {
                                        action.appendEffect(
                                                new AddUntilEndOfPhaseModifierEffect(
                                                        new ArcheryTotalModifier(self, Side.SHADOW, -cards.size())));
                                    }
                                }
                        );
                    }
                });
        return action;
    }
}
