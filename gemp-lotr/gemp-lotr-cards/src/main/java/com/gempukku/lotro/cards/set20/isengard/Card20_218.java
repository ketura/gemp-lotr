package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canPlayFromHand(self.getOwner(), game, -Filters.countActive(game, Culture.ISENGARD, Race.ORC), Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, PhysicalCard self) {
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
