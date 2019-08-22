package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardPhaseLimitEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. This minion is twilight cost -1 for each Wise character you spot (limit -2).
 * Skirmish: Remove (3) to make another [WRAITH] minion strength +2 (limit +4).
 */
public class Card32_074 extends AbstractMinion {
    public Card32_074() {
        super(6, 12, 3, 5, Race.NAZGUL, Culture.WRAITH, Names.toldea, "Revived", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -Math.min(2,Filters.countActive(game, Keyword.WISE));
    }
    
    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 3)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a WRAITH minion", Culture.WRAITH, CardType.MINION) {
                @Override
                public void cardSelected(LotroGame game, PhysicalCard card) {
                    action.insertEffect(
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.sameCard(card), null,
                                            new CardPhaseLimitEvaluator(game, self, Phase.SKIRMISH, 4, new ConstantEvaluator(2)))));
                }
            });
            return Collections.singletonList(action);
            
        }
        return null;
    }
}
