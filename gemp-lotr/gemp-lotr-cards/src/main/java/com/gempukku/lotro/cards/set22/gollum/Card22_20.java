package com.gempukku.lotro.cards.set22.gollum;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Minion
 * Strength: 5
 * Vitality: 4
 * Site: 4
 * Game Text: Each times Gollum wins a skirmish, you may add a doubt. Assignment: If Gollum is not roaming,
 * exert Gollum and assign him to Bilbo to make Gollum strength +1 (or +3 if at an underground site) until
 * the regroup phase.
 */
public class Card22_20 extends AbstractMinion {
    public Card22_20() {
        super(2, 5, 4, 4, null, Culture.GOLLUM, "Gollum", "Small Slimy Creature", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                new AddBurdenEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
	
	@Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
				&& PlayConditions.canExert(self, game, self)
                && !PlayConditions.canSpot(game, self, Keyword.ROAMING)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
			action.appendCost(
					new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, Filters.name("Bilbo")));
			action.appendEffect(
					new ChooseActiveCardEffect(self, playerId, "Gollum", Filters.name("Gollum")) {
                @Override
                protected void cardSelected(LotroGame game, PhysicalCard card) {
                    int bonus = game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.UNDERGROUND) ? 3 : 1;
                    action.insertEffect(
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.sameCard(card), bonus)));
                    }
});
}