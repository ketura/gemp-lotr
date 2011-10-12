package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: Southron. Archer. While you can spot another Southron, the minion archery total is +1 for each site
 * you control.
 */
public class Card4_245 extends AbstractMinion {
    public Card4_245() {
        super(3, 6, 2, 4, Race.MAN, Culture.RAIDER, "Southron Archer");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.SHADOW,
                        new SpotCondition(Filters.not(Filters.sameCard(self)), Filters.keyword(Keyword.SOUTHRON)),
                        new CountSpottableEvaluator(Filters.siteControlled(self.getOwner()))));
    }
}
