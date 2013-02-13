package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Gap of Rohan
 * 3	1
 * Plains. Sanctuary.
 * While you can spot a mounted companion, the move limit for this turn is +1.
 */
public class Card20_431 extends AbstractSite {
    public Card20_431() {
        super("Gap of Rohan", Block.SECOND_ED, 3, 1, null);
        addKeyword(Keyword.PLAINS);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (self.getWhileInZoneData() == null && PlayConditions.canSpot(game, CardType.COMPANION, Filters.mounted)) {
            self.setWhileInZoneData(new Object());
            game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                    new MoveLimitModifier(self, 1));
        }
        return null;
    }
}
