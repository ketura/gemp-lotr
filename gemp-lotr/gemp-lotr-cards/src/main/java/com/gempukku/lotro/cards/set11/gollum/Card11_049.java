package com.gempukku.lotro.cards.set11.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PlayNextSiteEffect;
import com.gempukku.lotro.cards.effects.PutPlayedEventIntoHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Fellowship or Regroup
 * Game Text: Spot Smeagol to play the fellowship's next site. Then you may add a burden to take this card back
 * into hand.
 */
public class Card11_049 extends AbstractEvent {
    public Card11_049() {
        super(Side.FREE_PEOPLE, 0, Culture.GOLLUM, "One Good Turn Deserves Another", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlayNextSiteEffect(action, playerId));
        action.appendEffect(
                new PlayoutDecisionEffect(playerId,
                        new YesNoDecision("Do you want to return " + GameUtils.getCardLink(self) + " back into hand?") {
                            @Override
                            protected void yes() {
                                SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                                subAction.appendCost(
                                        new AddBurdenEffect(self, 1));
                                subAction.appendEffect(
                                        new PutPlayedEventIntoHandEffect(action));
                                game.getActionsEnvironment().addActionToStack(subAction);
                            }
                        }));
        return action;
    }
}
