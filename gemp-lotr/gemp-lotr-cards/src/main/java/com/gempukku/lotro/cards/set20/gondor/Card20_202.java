package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * A Ranger's Adaptability
 * Event • Maneuver
 * Exert a [Gondor] ranger to exert a roaming minion twice (or exert any minion twice if at a site from your adventure deck).
 * http://www.lotrtcg.org/coreset/gondor/rangersadaptability(r2).jpg
 */
public class Card20_202 extends AbstractEvent {
    public Card20_202() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "A Ranger's Adaptability", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Keyword.RANGER));
        action.appendEffect(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2,
                        CardType.MINION, Filters.conditionFilter(Keyword.ROAMING, new LocationCondition(Filters.owner(playerId)), Filters.any)));
        return action;
    }
}
