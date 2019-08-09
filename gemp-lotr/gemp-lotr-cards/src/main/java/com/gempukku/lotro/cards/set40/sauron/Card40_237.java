package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignAgainstResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Orc Villain
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion - Orc
 * Strength: 8
 * Vitality: 3
 * Home: 6
 * Card Number: 1R237
 * Game Text: While you can spot another [SAURON] minion and cannot spot more than 2 Free Peoples cultures, each time
 * this minion is assigned to skirmish a companion, that companion must exert.
 */
public class Card40_237 extends AbstractMinion {
    public Card40_237() {
        super(3, 8, 3, 6, Race.ORC, Culture.SAURON, "Orc Villain", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, null, CardType.COMPANION, self)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION, Filters.not(self))
                && !PlayConditions.canSpotFPCultures(game, 3, self.getOwner())) {
            AssignAgainstResult assignedResult = (AssignAgainstResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, CardType.COMPANION, Filters.in(assignedResult.getAgainst())));
            return Collections.singletonList(action);
        }
        return null;
    }
}
