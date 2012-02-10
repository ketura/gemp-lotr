package com.gempukku.lotro.cards.set17.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Fellowship or Skirmish
 * Game Text: Spot 2 [GONDOR] companions and add a threat to discard a Shadow possession.
 */
public class Card17_032 extends AbstractEvent {
    public Card17_032() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Nimble Attack", Phase.FELLOWSHIP, Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.GONDOR, CardType.COMPANION)
                && PlayConditions.canAddThreat(game, self, 1);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddThreatsEffect(playerId, self, 1));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, CardType.POSSESSION));
        return action;
    }
}
