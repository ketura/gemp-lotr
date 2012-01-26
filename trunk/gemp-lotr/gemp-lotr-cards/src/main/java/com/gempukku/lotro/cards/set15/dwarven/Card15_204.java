package com.gempukku.lotro.cards.set15.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Archery: Exert Legolas and exert Aragorn to wound a minion. Skirmish: Exert Aragorn and spot Gimli to make
 * a minion strength -1 (or -3 if Aragorn and Gimli are hunters). Regroup: Exert Gimli and spot Legolas to draw a card
 * (or 2 cards if Gimli and Legolas are hunters).
 */
public class Card15_204 extends AbstractPermanent {
    public Card15_204() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Forth the Three Hunters!");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canExert(self, game, Filters.legolas)
                && PlayConditions.canExert(self, game, Filters.aragorn)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.legolas));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.aragorn));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Filters.aragorn)
                && PlayConditions.canSpot(game, Filters.gimli)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.aragorn));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new ConditionEvaluator(-1, -3,
                                    new AndCondition(
                                            new SpotCondition(Filters.aragorn, Keyword.HUNTER),
                                            new SpotCondition(Filters.gimli, Keyword.HUNTER))), CardType.MINION));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Filters.gimli)
                && PlayConditions.canSpot(game, Filters.legolas)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gimli));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            if (PlayConditions.canSpot(game, Filters.gimli, Keyword.HUNTER)
                    && PlayConditions.canSpot(game, Filters.legolas, Keyword.HUNTER))
                action.appendEffect(
                        new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
