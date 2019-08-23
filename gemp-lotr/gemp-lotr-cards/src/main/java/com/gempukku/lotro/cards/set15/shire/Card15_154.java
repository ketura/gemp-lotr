package com.gempukku.lotro.cards.set15.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Add a threat and exert your Ring-bearer to wound each non-hunter minion skirmishing
 * your [SHIRE] Ring-bearer.
 */
public class Card15_154 extends AbstractEvent {
    public Card15_154() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Second Breakfast", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canAddThreat(game, self, 1)
                && PlayConditions.canExert(self, game, Filters.owner(self.getOwner()), Filters.ringBearer);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddThreatsEffect(playerId, self, 1));
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Filters.ringBearer));
        action.appendEffect(
                new WoundCharactersEffect(self, CardType.MINION, Filters.not(Keyword.HUNTER), Filters.inSkirmishAgainst(Filters.owner(playerId), Culture.SHIRE, Filters.ringBearer)));
        return action;
    }
}
