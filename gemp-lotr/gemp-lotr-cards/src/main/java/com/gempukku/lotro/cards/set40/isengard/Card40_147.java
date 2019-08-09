package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Uruk Grunt
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion - Uruk-hai
 * Strength: 6
 * Vitality: 1
 * Home: 5
 * Card Number: 1C147
 * Game Text: Damage +1. While you can spot Saruman, this minion is strength +3.
 */
public class Card40_147 extends AbstractMinion {
    public Card40_147() {
        super(2, 6, 1, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Grunt");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self,
                new SpotCondition(Filters.saruman), 3);
        return Collections.singletonList(modifier);
    }
}
