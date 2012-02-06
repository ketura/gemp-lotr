package com.gempukku.lotro.cards.set15.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canAddThreat(game, self, 1)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Filters.ringBearer);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
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
