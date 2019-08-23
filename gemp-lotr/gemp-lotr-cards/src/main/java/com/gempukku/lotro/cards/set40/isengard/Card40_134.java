package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Saruman, Mind of Metal
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion - Wizard
 * Strength: 8
 * Vitality: 4
 * Home: 4
 * Card Number: 1U134
 * Game Text: Cunning. At the beginning of the maneuver phase, you may add (1) for each threat.
 * Shadow: Exert Saruman to play Saruman's Staff from your discard pile.
 */
public class Card40_134 extends AbstractMinion {
    public Card40_134() {
        super(4, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman", "Mind of Metal", true);
        addKeyword(Keyword.CUNNING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, new ForEachThreatEvaluator()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.name("Saruman's Staff"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.name("Saruman's Staff")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
