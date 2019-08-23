package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ❷ •I Will Help You Bear This Burden [Gan]
 * Condition • Companion
 * Strength: +2
 * Spell.
 * To play, exert Gandalf and remove up to 2 burdens. Plays on Frodo.
 * Discard this condition at the end of the turn.
 * <p/>
 * http://lotrtcg.org/coreset/gandalf/iwhybtb(r3).jpg
 */
public class Card20_163 extends AbstractAttachable {
    public Card20_163() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.GANDALF, null, "I Will Help You Bear This Burden", null, true);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.gandalf);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.frodo;
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new ExertExtraPlayCostModifier(self, self, null, Filters.gandalf),
                new AbstractExtraPlayCostModifier(self, "Extra cost to play", self) {
                    @Override
                    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                        return true;
                    }

                    @Override
                    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
                        action.appendCost(
                                new PlayoutDecisionEffect(card.getOwner(),
                                        new IntegerAwaitingDecision(1, "Choose number of burdens to remove", 0, Math.min(2, game.getGameState().getBurdens()), Math.min(2, game.getGameState().getBurdens())) {
                                            @Override
                                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                                action.appendCost(
                                                        new RemoveBurdenEffect(self.getOwner(), self, getValidatedResult(result)));
                                            }
                                        }));
                    }
                });
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfTurn(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
