package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventAllWoundsActionProxy;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 6
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Berserk Butcher is strength +1 for each wound on each character in its skirmish.
 * Maneuver: Remove an [ISENGARD] token from a machine and exert Berserk Butcher twice to prevent all wounds
 * to Uruk-hai until the assignment phase.
 */
public class Card6_060 extends AbstractMinion {
    public Card6_060() {
        super(6, 12, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Berserk Butcher", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.sameCard(self), Filters.inSkirmish),
                        null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
                                int wounds = 0;
                                for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.inSkirmish))
                                    wounds += gameState.getWounds(physicalCard);
                                return wounds;
                            }
                        }));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && PlayConditions.canRemoveTokens(game, Token.ISENGARD, 1, Keyword.MACHINE)
                && PlayConditions.canExert(self, game, 2, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ISENGARD, 1, Keyword.MACHINE));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseActionProxyEffect(
                            new PreventAllWoundsActionProxy(self, Race.URUK_HAI), Phase.ASSIGNMENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
