package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveBurderEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert a Hobbit companion to remove a burden.
 */
public class Card1_312 extends AbstractEvent {
    public Card1_312() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Sorry About Everything", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.HOBBIT), Filters.type(CardType.COMPANION), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose a Hobbit companion", Filters.keyword(Keyword.HOBBIT), Filters.type(CardType.COMPANION), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard hobbitCompanion) {
                        action.addCost(new ExertCharacterEffect(hobbitCompanion));
                    }
                });
        action.addEffect(new RemoveBurderEffect(playerId));
        return action;
    }
}
