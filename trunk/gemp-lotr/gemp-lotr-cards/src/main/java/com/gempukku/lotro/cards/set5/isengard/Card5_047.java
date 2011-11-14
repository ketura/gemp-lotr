package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 7
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Berserk Slayer is strength +2 for each wound on a character in its skirmish. Skirmish: Remove
 * 5 [ISENGARD] tokens from a machine and exert Berserk Slayer twice to wound every companion.
 */
public class Card5_047 extends AbstractMinion {
    public Card5_047() {
        super(7, 12, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Berserk Slayer", true);
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
                                return wounds * 2;
                            }
                        }));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(self, game, 2, Filters.sameCard(self))
                && PlayConditions.canRemoveTokens(game, Token.ISENGARD, 5, Keyword.MACHINE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ISENGARD, 5, Keyword.MACHINE));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new WoundCharactersEffect(self, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
