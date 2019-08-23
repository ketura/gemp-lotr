package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: To play, spot Gandalf. Each time the fellowship moves, add a threat or discard this condition.
 * Regroup: Discard this condition to wound 2 minions.
 */
public class Card7_048 extends AbstractPermanent {
    public Card7_048() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GANDALF, "Stay This Madness");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new AddThreatsEffect(self.getOwner(), self, 1));
            possibleEffects.add(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 2, 2, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
