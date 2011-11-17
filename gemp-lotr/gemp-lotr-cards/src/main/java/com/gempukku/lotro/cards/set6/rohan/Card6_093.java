package com.gempukku.lotro.cards.set6.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Regroup: Exert 2 mounted [ROHAN] Men to discard up to 2 wounded minions.
 */
public class Card6_093 extends AbstractEvent {
    public Card6_093() {
        super(Side.FREE_PEOPLE, 0, Culture.ROHAN, "Ever the Hope of Men", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, 1, 2, Culture.ROHAN, Race.MAN, Filters.mounted);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Culture.ROHAN, Race.MAN, Filters.mounted));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, CardType.MINION, Filters.wounded));
        return action;
    }
}
