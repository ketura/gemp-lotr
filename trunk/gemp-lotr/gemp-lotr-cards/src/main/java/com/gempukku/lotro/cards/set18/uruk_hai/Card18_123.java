package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: While you can spot 3 [URUK-HAI] hunters, at the start of each skirmish involving an [URUK-HAI] hunter,
 * each companion skirmishing that minion must exert.
 */
public class Card18_123 extends AbstractPermanent {
    public Card18_123() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Tracking the Prize", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, 3, Culture.URUK_HAI, Keyword.HUNTER)
                && PlayConditions.canSpot(game, Culture.URUK_HAI, Keyword.HUNTER, Filters.inSkirmish)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, CardType.COMPANION, Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }
}
