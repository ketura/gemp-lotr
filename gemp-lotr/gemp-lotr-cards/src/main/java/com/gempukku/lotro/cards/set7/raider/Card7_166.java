package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Archery: Spot 4 Free Peoples cultures and either exert this minion or remove a threat to wound
 * a companion (except the Ring-bearer).
 */
public class Card7_166 extends AbstractMinion {
    public Card7_166() {
        super(5, 10, 3, 4, Race.MAN, Culture.RAIDER, "Southron Leader");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && new CountCulturesEvaluator(0, 0, Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null) >= 4
                && (PlayConditions.canSelfExert(self, game) || PlayConditions.canRemoveThreat(game, self, 1))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ExertCharactersEffect(self, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert this minion";
                        }
                    });
            possibleCosts.add(
                    new RemoveThreatsEffect(self, 1) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a threat";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Keyword.RING_BEARER)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
