package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, 2, Filters.name("Theoden"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Theoden")));
        action.appendEffect(
                new HealCharactersEffect(playerId, Filters.and(CardType.COMPANION, Filters.not(Filters.name("Theoden")), Signet.THÉODEN)));
        return action;
    }
}
