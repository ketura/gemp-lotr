package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDoAssignmentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Assignment: Assign an exhausted companion (except the Ring-bearer) to skirmish a [MORIA] Orc.
 */
public class Card1_171 extends AbstractOldEvent {
    public Card1_171() {
        super(Side.SHADOW, Culture.MORIA, "Frenzy", Phase.ASSIGNMENT);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDoAssignmentEffect(action, playerId, Filters.and(Culture.MORIA, Race.ORC), Filters.and(CardType.COMPANION, Filters.exhausted, Filters.not(Filters.ringBearer))));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
