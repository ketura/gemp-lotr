package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Title: Binding Light
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event - Skirmish
 * Card Number: 1U66
 * Game Text: Spell. To play, spot Gandalf and another unbound companion.
 * The Shadow player may not play skirmish events or use skirmish special abilities during that companion's skirmish.
 */
public class Card40_066 extends AbstractEvent {
    public Card40_066() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Binding Light", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf)
                && PlayConditions.canSpot(game, Filters.inSkirmish, Filters.unboundCompanion, Filters.not(Filters.gandalf));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                new SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(self, Side.SHADOW, Phase.SKIRMISH), Phase.SKIRMISH));
        return action;
    }
}
