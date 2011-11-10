package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Return of the King
 * Type: Site
 * Site: 1K
 * Game Text: Each [GONDOR] companion is defender +1 until the end of the turn.
 */
public class Card7_334 extends AbstractSite {
    public Card7_334() {
        super("Steps of Edoras", Block.KING, 1, 0, Direction.LEFT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getData() == null) {
            self.storeData(new Object());
            game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                    new KeywordModifier(self, Filters.and(Culture.GONDOR, CardType.COMPANION), Keyword.DEFENDER, 1));
        }
        return null;
    }
}
