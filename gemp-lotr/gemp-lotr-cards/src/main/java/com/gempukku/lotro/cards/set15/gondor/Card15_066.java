package com.gempukku.lotro.cards.set15.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

import java.util.Collection;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: Exert a [GONDOR] Man to wound a minion (or wound that minion twice if it is a hunter).
 */
public class Card15_066 extends AbstractEvent {
    public Card15_066() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "No Travellers In This Land", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION) {
                    @Override
                    protected void woundedCardsCallback(Collection<PhysicalCard> cards) {
                        for (PhysicalCard physicalCard : Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), Keyword.HUNTER)) {
                            action.appendEffect(
                                    new WoundCharactersEffect(self, physicalCard));
                        }
                    }
                });
        return action;
    }
}
