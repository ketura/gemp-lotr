package com.gempukku.lotro.cards.set32.smaug;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Smaug
 * Twilight Cost: 2
 * Type: Condition • Support area
 * Game Text: Each time Smaug kills a character, you may play an Orc from your discard pile. Skirmish: Discard this
 * condition or exert Smaug twice to play a Shadow condition from your discard pile.
 */
public class Card32_063 extends AbstractPermanent {
    public Card32_063() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GUNDABAD, "Wrath of the Dragon", null, true);
    }
    
    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilledBy(game, effectResult, Filters.name("Smaug"), Filters.or(CardType.COMPANION, CardType.ALLY))
                && PlayConditions.canPlayFromDiscard(playerId, game, Race.ORC)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.CONDITION)
                && (PlayConditions.canExert(self, game, 2, Filters.name("Smaug"))
                 || PlayConditions.canSelfDiscard(self, game))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Smaug")) {
                @Override
                public String getText(LotroGame game) {
                    return "Exert Smaug twice";
                }
            });
            possibleCosts.add(
                    new SelfDiscardEffect(self) {
                @Override
                public String getText(LotroGame game) {
                    return "Discard Wrath of the Dragon";
                }
            });
            action.appendCost(new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(new ChooseAndPlayCardFromDiscardEffect(playerId, game, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
