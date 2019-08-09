package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.conditions.AndCondition;
import com.gempukku.lotro.logic.modifiers.conditions.NotCondition;
import com.gempukku.lotro.logic.modifiers.conditions.SpotCulturesCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Brute
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion - Orc
 * Strength: 8
 * Vitality: 3
 * Home: 6
 * Card Number: 1C226
 * Game Text: While you can spot another [SAURON] minion and cannot spot more than 2 Free Peoples cultures, this minion is strength +2.
 */
public class Card40_226 extends AbstractMinion {
    public Card40_226() {
        super(3, 8, 3, 6, Race.ORC, Culture.SAURON, "Orc Brute");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self,
                new AndCondition(
                        new SpotCondition(Culture.SAURON, CardType.MINION, Filters.not(self)),
                        new NotCondition(new SpotCulturesCondition(3, Side.FREE_PEOPLE))), 2);
        return Collections.singletonList(modifier);
    }
}
