package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Uruk Brute
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion - Uruk-hai
 * Strength: 9
 * Vitality: 2
 * Home: 5
 * Card Number: 1C146
 * Game Text: Damage +1. While at a battleground, this minion is strength +2.
 */
public class Card40_146 extends AbstractMinion {
    public Card40_146() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Brute");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self,
                new LocationCondition(Keyword.BATTLEGROUND), 2);
        return Collections.singletonList(modifier);
    }
}
