package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

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
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an ORC minion", Culture.ORC, CardType.MINION, Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new ChooseAndAssignCharacterToMinionEffect(action, playerId, card, CardType.COMPANION, Filters.not(Filters.ringBearer)));
                    }
                });
        return action;
    }
}
