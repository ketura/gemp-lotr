package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.PreventAllWoundsActionProxy;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(6, 12, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Berserk Butcher", null, true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(self, Filters.inSkirmish),
                        null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard self) {
                                int wounds = 0;
                                for (PhysicalCard physicalCard : Filters.filterActive(game, Filters.inSkirmish))
                                    wounds += game.getGameState().getWounds(physicalCard);
                                return wounds;
                            }
                        }));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canRemoveTokens(game, Token.ISENGARD, 1, Keyword.MACHINE)
                && PlayConditions.canExert(self, game, 2, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ISENGARD, 1, Keyword.MACHINE));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseActionProxyEffect(
                            new PreventAllWoundsActionProxy(self, Race.URUK_HAI), Phase.ASSIGNMENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
