package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: Marauding Uruk
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion - Uruk-hai
 * Strength: 8
 * Vitality: 3
 * Home: 5
 * Card Number: 1C126
 * Game Text: Damage +1. If you can spot 6 companions, this minion is fierce.
 */
public class Card40_126 extends AbstractMinion {
    public Card40_126() {
        super(3, 8, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Marauding Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, self,
                new SpotCondition(6, CardType.COMPANION), Keyword.FIERCE, 1);
        return Collections.singletonList(modifier);
    }
}
