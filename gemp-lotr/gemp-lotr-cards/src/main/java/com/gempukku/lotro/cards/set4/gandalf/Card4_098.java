package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Spell. Maneuver: Exert Gandalf to wound each minion who has strength of 6 or less.
 */
public class Card4_098 extends AbstractEvent {
    public Card4_098() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Mithrandir, Mithrandir!", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
        action.appendEffect(
                new WoundCharactersEffect(self,
                        Filters.and(
                                CardType.MINION,
                                new Filter() {
                                    @Override
                                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                        return game.getModifiersQuerying().getStrength(game, physicalCard) <= 6;
                                    }
                                })));
        return action;
    }
}
