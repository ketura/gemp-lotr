package com.gempukku.lotro.cards.set32.moria;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CardPhaseLimitEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 5
 * Type: Minion • Troll
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Skirmish: Exert this minion to make a [MORIA] minion strength +2 (limit +2).
 */
public class Card32_031 extends AbstractMinion {
    public Card32_031() {
        super(5, 10, 3, 4, Race.TROLL, Culture.MORIA, "Demolition Troll");
    }


    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Moria minion", Culture.MORIA, CardType.MINION) {
                        @Override
                        public void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), null,
                                                    new CardPhaseLimitEvaluator(game, self, Phase.SKIRMISH, 2, new ConstantEvaluator(2)))));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
