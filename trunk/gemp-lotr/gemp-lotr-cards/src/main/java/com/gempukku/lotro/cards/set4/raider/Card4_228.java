package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Easterling. Each time this minion is assigned to an unbound companion, you may exert him to add a burden.
 */
public class Card4_228 extends AbstractMinion {
    public Card4_228() {
        super(3, 8, 2, 4, Race.MAN, Culture.RAIDER, "Easterling Lieutenant");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.ASSIGNMENT
                && PlayConditions.canExert(self, game, Filters.sameCard(self))) {
            AssignmentResult result = (AssignmentResult) effectResult;
            for (Map.Entry<PhysicalCard, List<PhysicalCard>> oneAssignment : result.getAssignments().entrySet()) {
                if (oneAssignment.getValue().contains(self) && Filters.unboundCompanion().accepts(game.getGameState(), game.getModifiersQuerying(), oneAssignment.getKey())) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.appendCost(
                            new ExertCharactersEffect(self, self));
                    action.appendEffect(
                            new AddBurdenEffect(self, 1));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
