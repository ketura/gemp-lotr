package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Damage +1. While you control a site, the minion archery total is +1.
 */
public class Card4_138 extends AbstractMinion {
    public Card4_138() {
        super(5, 7, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Band of Uruk Bowmen");
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.SHADOW,
                        new SpotCondition(Filters.siteControlled(self.getOwner())), 1));
    }
}
