package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Arrays;
import java.util.List;

/**
 * Title: *Uruk Howlers
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion - Uruk-hai
 * Strength: 10
 * Vitality: 3
 * Home: 5
 * Card Number: 1R148
 * Game Text: Damage +1. While you can spot another Uruk-hai, this minion is fierce. While you can spot 2 other Uruk-hai, skip the archery phase.
 */
public class Card40_148 extends AbstractMinion {
    public Card40_148() {
        super(5, 10, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Howlers", null, true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier fierce = new KeywordModifier(self, self,
                new SpotCondition(Race.URUK_HAI, Filters.not(self)), Keyword.FIERCE, 1);
        ShouldSkipPhaseModifier skipArchery = new ShouldSkipPhaseModifier(self,
                new SpotCondition(2, Race.URUK_HAI, Filters.not(self)), Phase.ARCHERY);
        return Arrays.asList(fierce, skipArchery);
    }
}
