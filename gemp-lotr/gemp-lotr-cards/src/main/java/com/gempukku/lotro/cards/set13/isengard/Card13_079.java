package com.gempukku.lotro.cards.set13.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.TransferToShadowEffect;
import com.gempukku.lotro.cards.effects.TransferToSupportEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.IsAdditionalCardTypeModifier;
import com.gempukku.lotro.cards.modifiers.MayNotBearModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
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
 * Game Text: At the start of the maneuver phase, you may exert a Wizard to make this condition a fierce Wizard minion
 * until the start of the regroup phase that has 10 strength and 1 vitality, and cannot take wounds or bear other cards.
 * This card is still a condition.
 */
public class Card13_079 extends AbstractPermanent {
    public Card13_079() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Pallando Deceived", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && PlayConditions.canExert(self, game, Race.WIZARD)
                && self.getZone() != Zone.SHADOW_CHARACTERS) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.WIZARD));
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
                                            new MayNotBearModifier(self, self, Filters.any), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new CantTakeWoundsModifier(self, self), Phase.REGROUP));
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

    @Override
    public int getStrength() {
        return 10;
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
