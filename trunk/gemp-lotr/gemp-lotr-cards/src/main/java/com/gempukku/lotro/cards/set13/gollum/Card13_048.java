package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ResolveSkirmishEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Possession
 * Vitality: +1
 * Game Text: Bearer must be Smeagol. When you play Fishing Boat, you may reinforce a [GOLLUM] token. Each time Smeagol
 * is about to be overwhelmed in a skirmish, you may remove 2 [GOLLUM] tokens to make him strength +2 until the end of
 * that skirmish.
 */
public class Card13_048 extends AbstractAttachableFPPossession {
    public Card13_048() {
        super(2, 0, 1, Culture.GOLLUM, null, "Fishing Boat", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.smeagol;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.GOLLUM));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == Effect.Type.BEFORE_SKIRMISH_RESOLVED) {
            ResolveSkirmishEffect resolveEffect = (ResolveSkirmishEffect) effect;
            if (resolveEffect.getUpcomingResult(game) == ResolveSkirmishEffect.Result.FELLOWSHIP_OVERWHELMED
                    && game.getGameState().getSkirmish().getFellowshipCharacter() == self.getAttachedTo()) {
                if (PlayConditions.canRemoveTokens(game, Token.GOLLUM, 2, Filters.any)) {
                    ActivateCardAction action = new ActivateCardAction(self);
                    action.appendCost(
                            new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.GOLLUM, 2, Filters.any));
                    action.appendEffect(
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.hasAttached(self), 2), Phase.SKIRMISH));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
