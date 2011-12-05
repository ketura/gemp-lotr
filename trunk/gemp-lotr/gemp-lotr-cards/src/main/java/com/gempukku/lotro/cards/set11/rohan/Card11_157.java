package com.gempukku.lotro.cards.set11.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: To play, spot a [ROHAN] Man. Each time a Shadow condition is played, you may exert a minion.
 * Response: If a minion exerts as a cost of its special ability, discard this condition to prevent that and return
 * that minion to its owner's hand.
 */
public class Card11_157 extends AbstractPermanent {
    public Card11_157() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Rush of Steeds", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Side.SHADOW, CardType.CONDITION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachExerted(game, effectResult, CardType.MINION)
                && PlayConditions.canSelfDiscard(self, game)) {
            ExertResult exertResult = (ExertResult) effectResult;
            final PhysicalCard exertedCard = exertResult.getExertedCard();
            final Action exertingAction = exertResult.getAction();
            if (exertingAction != null && exertingAction.getType() == Action.Type.SPECIAL_ABILITY
                    && exertingAction.getActionSource() == exertedCard) {
                final ActivateCardAction activateAction = (ActivateCardAction) exertingAction;

                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                activateAction.prevent();
                            }
                        });
                action.appendEffect(
                        new ReturnCardsToHandEffect(self, exertedCard));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
