package com.gempukku.lotro.cards.set13.uruk_hai;

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
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: While you can spot 3 [URUK-HAI] lurker minions, at the start of each skirmish involving an [URUK-HAI]
 * lurker minion, each companion skirmishing that minion must exert.
 */
public class Card13_167 extends AbstractPermanent {
    public Card13_167() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Signs of War", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, Culture.URUK_HAI, Keyword.LURKER, Filters.inSkirmish)
                && PlayConditions.canSpot(game, 3, Culture.URUK_HAI, CardType.MINION, Keyword.LURKER)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(self, CardType.COMPANION, Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }
}
