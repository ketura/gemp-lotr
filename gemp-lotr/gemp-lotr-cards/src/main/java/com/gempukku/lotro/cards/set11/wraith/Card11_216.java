package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Maneuver: Remove (5) to make this condition a fierce [WRAITH] minion until the start of the regroup phase
 * that has 10 strength and 3 vitality, and cannot bear other cards. This card is still a condition. At the start of the
 * regroup phase, remove all tokens from here.
 */
public class Card11_216 extends AbstractPermanent {
    public Card11_216() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.WRAITH, "A Shadow Rises");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 5)
                && self.getZone() == Zone.SUPPORT) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(5));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self, Filters.attachedTo(self)));
            action.appendEffect(
                    new TransferToShadowEffect(self) {
                        @Override
                        protected void cardTransferredCallback() {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new VitalityModifier(self, self, 3), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, self, 10), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new IsAdditionalCardTypeModifier(self, self, CardType.MINION), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new MayNotBearModifier(self, self, Filters.any), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && self.getZone() == Zone.SHADOW_CHARACTERS) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            for (Map.Entry<Token, Integer> tokenCount : game.getGameState().getTokens(self).entrySet()) {
                                if (tokenCount.getValue() > 0)
                                    game.getGameState().removeTokens(self, tokenCount.getKey(), tokenCount.getValue());
                            }
                        }
                    });
            action.appendEffect(
                    new TransferToSupportEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
