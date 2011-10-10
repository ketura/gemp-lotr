package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
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
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot Saruman or an [ISENGARD] Man. Plays on a Free Peoples Man. Each time the Free Peoples
 * player assigns bearer to a skirmish, exert each ally.
 */
public class Card4_172 extends AbstractAttachable {
    public Card4_172() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.ISENGARD, null, "Rohan Is Mine");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Filters.or(Filters.name("Saruman"), Filters.and(Filters.culture(Culture.ISENGARD), Filters.race(Race.MAN))));
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.side(Side.FREE_PEOPLE), Filters.race(Race.MAN));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.ASSIGNMENT) {
            AssignmentResult assignmentResult = (AssignmentResult) effectResult;
            if (assignmentResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())
                    && assignmentResult.getAssignments().keySet().contains(self.getAttachedTo())) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new ExertCharactersEffect(self, Filters.type(CardType.ALLY)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
