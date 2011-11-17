package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Archery or Skirmish: Exert 2 Ring-bound Men to wound each roaming minion.
 */
public class Card4_110 extends AbstractOldEvent {
    public Card4_110() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Arrows Thick in the Air", Phase.ARCHERY, Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExertMultiple(self, game.getGameState(), game.getModifiersQuerying(), 1, 2, Filters.keyword(Keyword.RING_BOUND), Filters.race(Race.MAN));
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Filters.keyword(Keyword.RING_BOUND), Filters.race(Race.MAN)));
        action.appendEffect(
                new WoundCharactersEffect(self, Filters.and(CardType.MINION, Filters.keyword(Keyword.ROAMING))));
        return action;
    }
}
