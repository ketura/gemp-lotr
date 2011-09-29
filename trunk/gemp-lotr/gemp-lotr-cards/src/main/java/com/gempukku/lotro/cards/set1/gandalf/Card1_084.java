package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Spell. Fellowship: Exert Gandalf to discard every condition.
 */
public class Card1_084 extends AbstractEvent {
    public Card1_084() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Sleep Caradhras", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        PhysicalCard gandalf = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
        action.appendCost(new ExertCharactersCost(self, gandalf));
        action.appendEffect(
                new DiscardCardsFromPlayEffect(self, Filters.type(CardType.CONDITION)));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }
}
