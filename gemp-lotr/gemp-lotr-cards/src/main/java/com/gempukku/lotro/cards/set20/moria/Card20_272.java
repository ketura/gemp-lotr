package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * Goblin Senses
 * Moria	Event • Shadow
 * Spot a Goblin and a companion to add (1) for each card borne by that companion.
 */
public class Card20_272 extends AbstractEvent {
    public Card20_272() {
        super(Side.SHADOW, 0, Culture.MORIA, "Goblin Senses", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.GOBLIN)
                && PlayConditions.canSpot(game, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose a companion", Filters.spottable, CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int count = game.getGameState().getAttachedCards(card).size();
                        action.appendEffect(
                                new AddTwilightEffect(self, count));
                    }
                });
        return action;
    }
}
