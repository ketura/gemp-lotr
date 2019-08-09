package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 2, Filters.name(Names.theoden));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name(Names.theoden)));
        action.appendEffect(
                new HealCharactersEffect(self, Filters.and(CardType.COMPANION, Filters.not(Filters.name(Names.theoden)), Signet.THÉODEN)));
        action.appendEffect(
                new HealCharactersEffect(self, Filters.and(CardType.COMPANION, Filters.not(Filters.name(Names.theoden)), Signet.THÉODEN)));
        return action;
    }
}
