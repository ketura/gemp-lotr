package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

import java.util.Collections;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a Hobbit bearing a weapon to wound a minion. That minion's owner may remove (3)
 * to prevent this.
 */
public class Card2_103 extends AbstractOldEvent {
    public Card2_103() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Hobbit Sword-play", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT), Filters.hasAttached(Filters.weapon));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.HOBBIT), Filters.hasAttached(Filters.weapon)));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose minion", Filters.type(CardType.MINION)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new PreventableEffect(action,
                                        new WoundCharactersEffect(self, card),
                                        Collections.singletonList(card.getOwner()),
                                        new RemoveTwilightEffect(3)));
                    }
                });
        return action;
    }
}
