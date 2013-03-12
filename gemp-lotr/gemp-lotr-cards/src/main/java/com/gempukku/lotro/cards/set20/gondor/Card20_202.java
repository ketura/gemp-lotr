package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExhaustCharactersEffect;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * A Ranger's Adaptability
 * Gondor	Event • Maneuver
 * Spot a [Gondor] ranger to exhaust a roaming minion (or any minion if at a site from your adventure deck).
 */
public class Card20_202 extends AbstractEvent {
    public Card20_202() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "A Ranger's Adaptability", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndExhaustCharactersEffect(action, playerId, 1, 1,
                        CardType.MINION, Filters.conditionFilter(Keyword.ROAMING, new LocationCondition(Filters.owner(playerId)), Filters.any)));
        return action;
    }
}
