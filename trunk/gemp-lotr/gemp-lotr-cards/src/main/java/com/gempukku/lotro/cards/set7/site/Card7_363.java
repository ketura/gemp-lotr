package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 9
 * Type: Site
 * Site: 9K
 * Game Text: Each time a companion is killed, add a burden.
 */
public class Card7_363 extends AbstractSite {
    public Card7_363() {
        super("Slag Mounds", Block.KING, 9, 9, Direction.LEFT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilled(game, effectResult, CardType.COMPANION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
