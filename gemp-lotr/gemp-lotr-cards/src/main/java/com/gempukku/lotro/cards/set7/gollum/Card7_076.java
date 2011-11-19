package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: Play Smeagol from your discard pile to play the fellowship's next site (replacing an opponent's site if
 * necessary). If you do not move again this turn, discard your hand.
 */
public class Card7_076 extends AbstractEvent {
    public Card7_076() {
        super(Side.FREE_PEOPLE, 0, Culture.GOLLUM, "Very Nice Friends", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.smeagol));
        action.appendEffect(
                new PlaySiteEffect(playerId, null, game.getGameState().getCurrentSiteNumber() + 1));
        final int moveCount = game.getGameState().getMoveCount();
        action.appendEffect(
                new AddUntilEndOfPhaseActionProxyEffect(
                        new AbstractActionProxy() {
                            @Override
                            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                if (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                                        && game.getGameState().getCurrentPhase() == Phase.REGROUP
                                        && moveCount == game.getGameState().getMoveCount()) {
                                    RequiredTriggerAction action = new RequiredTriggerAction(self);
                                    action.appendEffect(
                                            new DiscardCardsFromHandEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId)), true));
                                    return Collections.singletonList(action);
                                }
                                return null;
                            }
                        }, Phase.REGROUP));
        return action;
    }
}
