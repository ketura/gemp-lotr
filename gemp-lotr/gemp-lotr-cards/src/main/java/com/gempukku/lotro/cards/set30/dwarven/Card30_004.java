package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a Dwarf character to wound 2 Orcs or to wound 1 Orc twice.
 */
public class Card30_004 extends AbstractEvent {
    public Card30_004() {//
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Battle of Azanulbizar", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.DWARF, Filters.character);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF, Filters.character));
        action.appendEffect(
                new ChooseActiveCardsEffect(self, playerId, "Choose Orc(s) to wound", 1, 2, Race.ORC) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        if (cards.size() == 2) {
                            action.appendEffect(
                                    new WoundCharactersEffect(self, Filters.in(cards)));
                        } else {
                            action.appendEffect(new WoundCharactersEffect(self, Filters.in(cards)));
                            action.appendEffect(new WoundCharactersEffect(self, Filters.in(cards)));
                        }
                    }
                }
        );
        return action;
    }
}
