package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 [GANDALF] companions. At the start of the maneuver phase, discard this condition to make
 * each player count the number of cards in his or her hand, discard each of them and draw the same number of cards from
 * the top of his or her draw deck.
 */
public class Card15_031 extends AbstractPermanent {
    public Card15_031() {
        super(Side.FREE_PEOPLE, 4, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Mellon!", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.GANDALF, CardType.COMPANION);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && PlayConditions.canSelfDiscard(self, game)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            for (String playerId : game.getGameState().getPlayerOrder().getAllPlayers()) {
                int handSize = game.getGameState().getHand(playerId).size();
                action.appendEffect(
                        new DiscardCardsFromHandEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId)), false));
                action.appendEffect(
                        new DrawCardsEffect(action, playerId, handSize));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
