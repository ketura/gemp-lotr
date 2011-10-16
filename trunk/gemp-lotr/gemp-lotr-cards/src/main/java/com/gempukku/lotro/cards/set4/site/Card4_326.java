package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Two Towers
 * Type: Site
 * Site: 1T
 * Game Text: Plains. While you can spot a [ROHAN] mount at Horse-country, the move limit is +1 for this turn.
 */
public class Card4_326 extends AbstractSite {
    public Card4_326() {
        super("Horse-country", Block.TWO_TOWERS, 1, 0, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getData() == null
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Culture.ROHAN, PossessionClass.MOUNT)) {
            self.storeData(new Object());
            game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                    new MoveLimitModifier(self, 1));
        }
        return null;
    }
}
