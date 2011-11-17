package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. Plays on a companion or ally. Limit 1 per character. Each time the Free Peoples player assigns
 * bearer to skirmish an [ISENGARD] tracker, bearer must exert.
 */
public class Card4_212 extends AbstractAttachable {
    public Card4_212() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.ISENGARD, null, "Weary");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.or(CardType.COMPANION, CardType.ALLY), Filters.not(Filters.hasAttached(Filters.name("Weary"))));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.ASSIGNMENT) {
            AssignmentResult assignmentResult = (AssignmentResult) effectResult;
            if (assignmentResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())) {
                if (assignmentResult.getAssignments().keySet().contains(self.getAttachedTo())) {
                    if (Filters.filter(assignmentResult.getAssignments().get(self.getAttachedTo()), game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, Keyword.TRACKER).size() > 0) {
                        RequiredTriggerAction action = new RequiredTriggerAction(self);
                        action.appendEffect(
                                new ExertCharactersEffect(self, self.getAttachedTo()));
                        return Collections.singletonList(action);
                    }
                }
            }
        }
        return null;
    }
}
