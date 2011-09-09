package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Play a Nazgul. His twilight cost is -2.
 */
public class Card1_217 extends AbstractEvent {
    public Card1_217() {
        super(Side.SHADOW, Culture.WRAITH, "Morgul Gates", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseCardsFromHandEffect(playerId, "Choose an Elf to play", 1, 1, Filters.keyword(Keyword.NAZGUL), Filters.playable(game, -2)) {
                    @Override
                    protected void cardsSelected(List<PhysicalCard> selectedCards) {
                        PhysicalCard selectedCard = selectedCards.get(0);
                        game.getActionsEnvironment().addActionToStack(
                                selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, -2));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
