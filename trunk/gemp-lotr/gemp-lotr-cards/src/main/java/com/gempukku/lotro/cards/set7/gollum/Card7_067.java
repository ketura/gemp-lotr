package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 5
 * Type: Condition • Support Area
 * Game Text: To play, spot Gollum and add up to 9 threats. Gollum is strength +2. Discard this condition and remove
 * 9 threats at the start of the regroup phase.
 */
public class Card7_067 extends AbstractPermanent {
    public Card7_067() {
        super(Side.SHADOW, 5, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Plotting", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, Filters.gollum);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier);
        int maxThreats = Math.min(9, Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) - game.getGameState().getThreats());
        playCardAction.appendCost(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new IntegerAwaitingDecision(1, "Choose how many threats to add", 0, maxThreats) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int threats = getValidatedResult(result);
                                playCardAction.appendCost(
                                        new AddThreatsEffect(playerId, self, threats));
                            }
                        })
        );
        return playCardAction;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.gollum, 2));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.REGROUP) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new RemoveThreatsEffect(self, 9));
            return Collections.singletonList(action);
        }
        return null;
    }
}
