package com.gempukku.lotro.cards.set22.gundabad;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.CancelActivatedEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 4
 * Type: Possession • Mount
 * Strength: +4
 * Vitality: +2
 * Game Text: Bearer must a [GUNDABAD] Orc. Bearer is fierce. Assignment: Exert bearer twice or remove 2
 * doubts to assign bearer to Thorin.
 */
public class Card22_26 extends AbstractAttachable {
    public Card22_26() {
        super(Side.SHADOW, CardType.POSSESSION, 4, Culture.Gundabad, PossessionClass.MOUNT, "Threatening Warg");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 4));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        return modifiers;
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GUNDABAD,Race.ORC);
    }
	
	@Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
					&& (&& PlayConditions.canExert(self, game, 2, self.getAttachedTo())
						|| game.getGameState().getBurdens() >= 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
			List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new SelfExertEffect(action, self.getAttachedTo()) {
                @Override
                public String getText(LotroGame game) {
                    return "Exert bearer twice";
                }
            });
            possibleCosts.add(
					new RemoveBurdenEffect(playerId, self, 2)); {
                @Override
                public String getText(LotroGame game) {
                    return "Remove two doubts";
                }
            });
            action.appendCost(
					new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self.getAttachedTo(), Filters.name("Thorin")));
            return Collections.singletonList(action);
        }
        return null;
	}
}
