package com.gempukku.lotro.cards.set13.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Event • Shadow
 * Game Text: Toil 1. (For each [WRAITH] character you exert when playing this, it is twilight cost -1.) To play, spot
 * a [WRAITH] card in your support area. Spot a companion to draw a card for each wound on that companion (and draw
 * an additional card if you can spot a Nazgul).
 */
public class Card13_180 extends AbstractEvent {
    public Card13_180() {
        super(Side.SHADOW, 4, Culture.WRAITH, "Shadow in the East", Phase.SHADOW);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.owner(playerId), Culture.WRAITH, Zone.SUPPORT)
                && PlayConditions.canSpot(game, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int count = game.getGameState().getWounds(card);
                        if (PlayConditions.canSpot(game, Race.NAZGUL))
                            count++;
                        action.appendEffect(
                                new DrawCardsEffect(action, playerId, count));
                    }
                });
        return action;
    }
}
