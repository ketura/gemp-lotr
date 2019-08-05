package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Uruk Battlefiend
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion - Uruk-hai
 * Strength: 8
 * Vitality: 2
 * Home: 5
 * Card Number: 1U144
 * Game Text: Damage +1. This minion is strength +1 for each battleground in the current region.
 */
public class Card40_144 extends AbstractMinion {
    public Card40_144() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Battlefiend");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self, null,
                new CountSpottableEvaluator(Keyword.BATTLEGROUND, Filters.siteInCurrentRegion));
        return Collections.singletonList(modifier);
    }
}
