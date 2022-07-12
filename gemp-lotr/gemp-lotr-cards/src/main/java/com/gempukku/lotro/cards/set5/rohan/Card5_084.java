package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Fellowship: Exert Theoden twice to heal every other companion with the Theoden signet twice.
 */
public class Card5_084 extends AbstractEvent {
    public Card5_084() {
        super(Side.FREE_PEOPLE, 3, Culture.ROHAN, "I Am Here", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 2, Filters.name(Names.theoden));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name(Names.theoden)));
        action.appendEffect(
                new HealCharactersEffect(self, self.getOwner(), Filters.and(CardType.COMPANION, Filters.not(Filters.name(Names.theoden)), Signet.THEODEN)));
        action.appendEffect(
                new HealCharactersEffect(self, self.getOwner(), Filters.and(CardType.COMPANION, Filters.not(Filters.name(Names.theoden)), Signet.THEODEN)));
        return action;
    }
}
