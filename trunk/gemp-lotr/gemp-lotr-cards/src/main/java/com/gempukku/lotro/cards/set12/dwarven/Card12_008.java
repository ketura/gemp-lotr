package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to make a Dwarf strength +1 for each of the following that is true:
 * he is at a mountain or underground site; he has resistance 3 or more; he is bearing a possession; he is skirmishing
 * a fierce minion.
 */
public class Card12_008 extends AbstractPermanent {
    public Card12_008() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "His Father's Charge");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new Evaluator() {
                                @Override
                                public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                    int bonus = 0;
                                    if (PlayConditions.location(game, Filters.or(Keyword.MOUNTAIN, Keyword.UNDERGROUND)))
                                        bonus++;
                                    if (Filters.minResistance(3).accepts(gameState, modifiersQuerying, cardAffected))
                                        bonus++;
                                    if (Filters.hasAttached(CardType.POSSESSION).accepts(gameState, modifiersQuerying, cardAffected))
                                        bonus++;
                                    if (Filters.inSkirmishAgainst(CardType.MINION, Keyword.FIERCE).accepts(gameState, modifiersQuerying, cardAffected))
                                        bonus++;
                                    return bonus;
                                }
                            }, Race.DWARF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
