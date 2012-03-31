package com.gempukku.lotro.cards.set0.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Promotional
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Skirmish: Play a [GONDOR] skirmish event to make each minion skirmishing Anarion strength -X, where X is
 * Anarion's vitality.
 */
public class Card0_058 extends AbstractCompanion {
    public Card0_058() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, null, "Anarion", "Lord of Anorien", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.GONDOR, CardType.EVENT, Keyword.SKIRMISH)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.GONDOR, CardType.EVENT, Keyword.SKIRMISH));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(self)), null,
                                    new Evaluator() {
                                        @Override
                                        public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                            return -modifiersQuerying.getVitality(game.getGameState(), self);
                                        }
                                    }), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
