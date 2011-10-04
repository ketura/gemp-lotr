package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

import java.util.Collection;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Exert a [SAURON] Orc to wound a character he is skirmishing.
 */
public class Card1_255 extends AbstractEvent {
    public Card1_255() {
        super(Side.SHADOW, Culture.SAURON, "Mordor's Strength", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.SAURON), Filters.race(Race.ORC)) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> minions) {
                        super.cardsSelected(game, minions);    //To change body of overridden methods use File | Settings | File Templates.
                        if (minions.size() > 0) {
                            PhysicalCard minion = minions.iterator().next();
                            action.appendEffect(
                                    new WoundCharactersEffect(self, Filters.inSkirmishAgainst(Filters.sameCard(minion))));
                        }
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
