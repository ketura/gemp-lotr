package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Title: Goblin Senses
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event - Shadow
 * Card Number: 1U173
 * Game Text: Spot a [MORIA] Goblin and a companion to ad (1) for each Free Peoples card borne by that companion.
 */
public class Card40_173 extends AbstractEvent {
    public Card40_173() {
        super(Side.SHADOW, 0, Culture.MORIA, "Goblin Senses", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.MORIA, Race.GOBLIN)
                && PlayConditions.canSpot(game, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        final int cardCount = Filters.filter(game.getGameState().getAttachedCards(card), game, Side.FREE_PEOPLE).size();
                        action.appendEffect(
                                new AddTwilightEffect(self, cardCount));
                    }
                });
        return action;
    }
}
