package com.gempukku.lotro.cards.set12.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, spot a [GANDALF] companion. Bearer must be a companion (except the Ring-bearer).
 * Limit 1 per bearer. Each time bearer wins a skirmish, you may remove a burden.
 */
public class Card12_035 extends AbstractAttachable {
    public Card12_035() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.GANDALF, null, "Watch and Wait");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GANDALF, CardType.COMPANION);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Filters.not(Filters.hasAttached(Filters.name(getName()))), Filters.not(Filters.ringBearer));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
