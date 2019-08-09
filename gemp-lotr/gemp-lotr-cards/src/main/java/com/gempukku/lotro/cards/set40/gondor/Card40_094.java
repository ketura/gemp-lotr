package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Aragorn, Dunedain Ranger
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion - Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1C94
 * Game Text: Ranger. While at a site from your adventure deck, Aragorn is defender +1.
 */
public class Card40_094 extends AbstractCompanion {
    public Card40_094() {
        super(4, 8, 4, 8, Culture.GONDOR, Race.MAN, null, "Aragorn", "Dunedain Ranger", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, self,
                new LocationCondition(Filters.owner(self.getOwner())), Keyword.DEFENDER, 1);
        return Collections.singletonList(modifier);
    }
}
