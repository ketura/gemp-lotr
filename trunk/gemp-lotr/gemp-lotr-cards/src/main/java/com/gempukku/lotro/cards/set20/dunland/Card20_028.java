package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantBeAssignedAgainstModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * 2
 * Sneak Attack
 * Dunland	Event • Assignment
 * Spot a [Dunland] Man and a site you control to spot an unbound companion. Until the regroup phase,
 * that companion may not be assigned to skirmish a [Dunland] man.
 */
public class Card20_028 extends AbstractEvent {
    public Card20_028() {
        super(Side.SHADOW, 2, Culture.DUNLAND, "Sneak Attack", Phase.ASSIGNMENT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.DUNLAND, Race.MAN)
                && PlayConditions.canSpot(game, Filters.siteControlled(playerId));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard companion) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                new CantBeAssignedAgainstModifier(self, null, companion, Filters.and(Culture.DUNLAND, Race.MAN)), Phase.REGROUP));
                    }
                });
        return action;
    }
}
