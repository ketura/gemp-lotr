package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

/**
 * 0
 * Orcish Labor
 * Isengard	Event • Shadow
 * Exert X [Isengard] Orcs to play a Uruk-hai; it’s twilight cost is - X.
 */
public class Card20_218 extends AbstractEvent {
    public Card20_218() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Orcish Labor", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canPlayFromHand(playerId, game, -Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, Race.ORC), Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 0, Integer.MAX_VALUE, Culture.ISENGARD, Race.ORC) {
                    @Override
                    protected void cardsToBeExertedCallback(Collection<PhysicalCard> characters) {
                        int count = characters.size();
                        action.appendEffect(
                                new ChooseAndPlayCardFromHandEffect(playerId, game, -count, Race.URUK_HAI));
                    }
                });
        return action;
    }
}
