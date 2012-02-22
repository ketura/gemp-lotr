package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. While this minion is assigned to a skirmish, each site on the adventure path gains each terrain
 * keyword from each site you control.
 */
public class Card17_120 extends AbstractMinion {
    public Card17_120() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Attacker");
        addKeyword(Keyword.DAMAGE, 1);
    }


    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        for (Keyword keyword : Keyword.values()) {
            if (keyword.isTerrain())
                modifiers.add(
                        new KeywordModifier(self, Filters.and(CardType.SITE, Zone.ADVENTURE_PATH),
                                new AndCondition(
                                        new SpotCondition(self, Filters.assignedToSkirmish),
                                        new SpotCondition(Filters.siteControlled(self.getOwner()), keyword)), keyword, 1));
        }
        return modifiers;
    }
}
