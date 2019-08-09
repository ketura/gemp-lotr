package com.gempukku.lotro.cards.set3.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Twilight Cost: 2
 * Type: Site
 * Site: 4
 * Game Text: When the fellowship moves to Eregion Hills, add a burden.
 */
public class Card3_116 extends AbstractSite {
    public Card3_116() {
        super("Eregion Hills", SitesBlock.FELLOWSHIP, 4, 2, Direction.RIGHT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
