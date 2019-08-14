package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Spot a [GONDOR] companion to reinforce a [GONDOR] token for each possession borne by that companion.
 */
public class Card13_068 extends AbstractEvent {
    public Card13_068() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Guarded City", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR companion", Culture.GONDOR, CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int count = Filters.countActive(game, CardType.POSSESSION, Filters.attachedTo(card));
                        for (int i = 0; i < count; i++)
                            action.appendEffect(
                                    new ReinforceTokenEffect(self, playerId, Token.GONDOR));
                    }
                });
        return action;
    }
}
