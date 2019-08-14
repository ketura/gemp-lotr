package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Title: Open War
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1C131
 * Game Text: Make an Uruk-hai strength +1 for each battleground in the current region.
 */
public class Card40_131 extends AbstractEvent {
    public Card40_131() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Open War", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CountSpottableEvaluator(Keyword.BATTLEGROUND, Filters.siteInCurrentRegion), Race.URUK_HAI));
        return action;
    }
}
