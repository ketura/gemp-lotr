package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 0
 * Type: Event • Response
 * Game Text: If the Free Peoples player plays a companion, discard a [MEN] card from hand to return that companion
 * to its owner's hand.
 */
public class Card18_065 extends AbstractResponseEvent {
    public Card18_065() {
        super(Side.SHADOW, 0, Culture.MEN, "Declined Business");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canDiscardCardsFromHandToPlay(self, game, playerId, 1, Culture.MEN);
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.owner(game.getGameState().getCurrentPlayerId()), CardType.COMPANION)
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.MEN));
            action.appendEffect(
                    new ReturnCardsToHandEffect(self, ((PlayCardResult) effectResult).getPlayedCard()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
