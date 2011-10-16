package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 3
 * Type: Site
 * Site: 2T
 * Game Text: Plains. Each time the Free Peoples player assigns a companion or ally to a skirmish, add (1).
 */
public class Card4_336 extends AbstractSite {
    public Card4_336() {
        super("Wold of Rohan", Block.TWO_TOWERS, 2, 3, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.ASSIGNMENT) {
            AssignmentResult assignmentResult = (AssignmentResult) effectResult;
            if (assignmentResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())) {
                int assignedCount = assignmentResult.getAssignments().keySet().size();
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddTwilightEffect(self, assignedCount));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
