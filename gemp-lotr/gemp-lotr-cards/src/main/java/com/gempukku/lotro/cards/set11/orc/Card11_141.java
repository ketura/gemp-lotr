package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDoAssignmentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 8
 * Type: Event • Assignment
 * Game Text: Toil 3. (For each [ORC] character you exert when playing this, its twilight cost is -3.) Assign an [ORC]
 * minion to a companion (except the Ring-bearer).
 */
public class Card11_141 extends AbstractEvent {
    public Card11_141() {
        super(Side.SHADOW, 8, Culture.ORC, "Undisciplined", Phase.ASSIGNMENT);
        addKeyword(Keyword.TOIL, 3);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDoAssignmentEffect(action, playerId, Culture.ORC, Filters.and(CardType.COMPANION, Filters.not(Filters.ringBearer))));
        return action;
    }
}
