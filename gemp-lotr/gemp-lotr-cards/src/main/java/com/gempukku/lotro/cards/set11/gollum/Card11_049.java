package com.gempukku.lotro.cards.set11.gollum;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.PlayNextSiteEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.PutPlayedEventIntoHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self) {
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
                                        new AddBurdenEffect(playerId, self, 1));
                                subAction.appendEffect(
                                        new PutPlayedEventIntoHandEffect(action));
                                game.getActionsEnvironment().addActionToStack(subAction);
                            }
                        }));
        return action;
    }
}
