package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.logic.modifiers.condition.SpotCulturesCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Monstrosity
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion - Orc
 * Strength: 11
 * Vitality: 3
 * Home: 6
 * Card Number: 1R229
 * Game Text: While you can spot another [SAURON] Orc and cannot spot more than 2 Free Peoples cultures, this minion is damage +1.
 */
public class Card40_229 extends AbstractMinion {
    public Card40_229() {
        super(4, 11, 3, 6, Race.ORC, Culture.SAURON,"Orc Monstrosity");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, self,
                new AndCondition(
                        new SpotCondition(Culture.SAURON, Race.ORC, Filters.not(self)),
                        new NotCondition(new SpotCulturesCondition(3, Side.FREE_PEOPLE))
                ), Keyword.DAMAGE, 1);
        return Collections.singletonList(modifier);
    }
}
