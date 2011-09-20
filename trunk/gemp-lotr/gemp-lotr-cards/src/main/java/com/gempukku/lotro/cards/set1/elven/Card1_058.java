package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert 2 Elves to discard a condition.
 */
public class Card1_058 extends AbstractEvent {
    public Card1_058() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "The Seen and the Unseen", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF), Filters.canExert()) >= 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardsEffect(playerId, "Choose elves to exert", 2, 2, Filters.race(Race.ELF), Filters.canExert()) {
                    @Override
                    protected void cardsSelected(List<PhysicalCard> cards) {
                        action.addCost(new ExertCharacterEffect(playerId, Filters.in(cards)));
                    }
                });
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose condition", Filters.type(CardType.CONDITION)) {
                    @Override
                    protected void cardSelected(PhysicalCard condition) {

                    }
                });
        return action;
    }
}
