package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 2
 * Site: 3
 * Game Text: Each time the Free Peoples player assigns a companion or ally to an [ISENGARD] Man, that companion
 * or ally must exert.
 */
public class Card4_178 extends AbstractMinion {
    public Card4_178() {
        super(2, 6, 2, 3, Race.MAN, Culture.ISENGARD, "Unferth", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.ASSIGNMENT) {
            AssignmentResult assignmentResult = (AssignmentResult) effectResult;
            if (assignmentResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())) {
                List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
                for (Map.Entry<PhysicalCard, List<PhysicalCard>> physicalCardListEntry : assignmentResult.getAssignments().entrySet()) {
                    if (Filters.filter(physicalCardListEntry.getValue(), game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, Race.MAN).size() > 0) {
                        RequiredTriggerAction action = new RequiredTriggerAction(self);
                        action.appendEffect(
                                new ExertCharactersEffect(self, physicalCardListEntry.getKey()));
                        actions.add(action);
                    }
                }

                return actions;
            }
        }
        return null;
    }
}
