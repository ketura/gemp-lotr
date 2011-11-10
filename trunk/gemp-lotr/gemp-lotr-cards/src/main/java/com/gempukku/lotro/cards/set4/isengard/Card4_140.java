package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Maneuver: Exert 2 Uruk-hai to add a burden for each card in the dead pile.
 */
public class Card4_140 extends AbstractOldEvent {
    public Card4_140() {
        super(Side.SHADOW, Culture.ISENGARD, "Beyond All Hope", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExertMultiple(self, game.getGameState(), game.getModifiersQuerying(), 1, 2, Filters.race(Race.URUK_HAI));
    }

    @Override
    public int getTwilightCost() {
        return 5;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Filters.race(Race.URUK_HAI)));
        action.appendEffect(
                new AddBurdenEffect(self, game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()).size()));
        return action;
    }
}
