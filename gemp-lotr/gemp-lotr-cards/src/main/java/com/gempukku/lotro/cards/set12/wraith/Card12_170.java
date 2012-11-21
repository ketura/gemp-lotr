package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, spot a [WRAITH] minion. Bearer must be an unbound companion. Limit 1 per bearer. At the end of
 * each assignment phase, if bearer is not assigned to a skirmish and a companion who does not bear a [WRAITH] condition
 * is, add a burden.
 */
public class Card12_170 extends AbstractAttachable {
    public Card12_170() {
        super(Side.SHADOW, CardType.CONDITION, 2, Culture.WRAITH, null, "Sense of Obligation");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Culture.WRAITH, CardType.MINION);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.unboundCompanion, Filters.not(Filters.hasAttached(Filters.name(getName()))));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfPhase(game, effectResult, Phase.ASSIGNMENT)
                && Filters.notAssignedToSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.assignedToSkirmish, Filters.not(Filters.hasAttached(Culture.WRAITH, CardType.CONDITION)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
