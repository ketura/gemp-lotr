package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 Nazgul and add up to 3 threats. Each time a companion is about to be killed, you may
 * remove a threat to discard up to 2 possessions. Discard this condition and remove 3 threats during the regroup phase.
 */
public class Card7_181 extends AbstractPermanent {
    public Card7_181() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Held Ground", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Race.NAZGUL);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        int maxThreats = Math.min(3, Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) - game.getGameState().getThreats());
        permanentAction.appendCost(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new IntegerAwaitingDecision(1, "Choose how many threats to add", 0, maxThreats) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int threats = getValidatedResult(result);
                                permanentAction.appendCost(
                                        new AddThreatsEffect(playerId, self, threats));
                            }
                        })
        );
        return permanentAction;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingKilled(effect, game, CardType.COMPANION)) {
            KillEffect killEffect = (KillEffect) effect;
            Collection<PhysicalCard> companionsKilled = Filters.filter(killEffect.getCharactersToBeKilled(), game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
            for (PhysicalCard physicalCard : companionsKilled) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendCost(
                        new RemoveThreatsEffect(self, 1));
                action.appendEffect(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, CardType.POSSESSION));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new RemoveThreatsEffect(self, 3));
            return Collections.singletonList(action);
        }
        return null;
    }
}
