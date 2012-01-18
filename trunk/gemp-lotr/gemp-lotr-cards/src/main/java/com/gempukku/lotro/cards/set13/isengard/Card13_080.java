package com.gempukku.lotro.cards.set13.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.TransferToShadowEffect;
import com.gempukku.lotro.cards.effects.TransferToSupportEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.IsAdditionalCardTypeModifier;
import com.gempukku.lotro.cards.modifiers.MayNotBearModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: Each time a Wizard heals, this condition becomes a fierce Wizard minion until the start of the regroup
 * phase that has 12 strength and 1 vitality, and cannot take wounds or bear other cards.
 * This card is still a condition.
 */
public class Card13_080 extends AbstractPermanent {
    public Card13_080() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Radagast Deceived", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.forEachHealed(game, effectResult, Race.WIZARD)
                && self.getZone() != Zone.SHADOW_CHARACTERS) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new TransferToShadowEffect(self) {
                        @Override
                        protected void cardTransferredCallback() {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new IsAdditionalCardTypeModifier(self, self, CardType.MINION), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, self, Keyword.DAMAGE, 1), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new MayNotBearModifier(self, self, Filters.any), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new CantTakeWoundsModifier(self, self), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
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

    @Override
    public int getStrength() {
        return 12;
    }

    @Override
    public int getVitality() {
        return 1;
    }

    @Override
    public Race getRace() {
        return Race.WIZARD;
    }

    @Override
    public int getSiteNumber() {
        return 0;
    }
}
