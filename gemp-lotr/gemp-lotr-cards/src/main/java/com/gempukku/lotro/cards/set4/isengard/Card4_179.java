package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 7
 * Type: Minion • Uruk-Hai
 * Strength: 14
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. To play, spot an Uruk-hai. While at a battleground, this minion is fierce. While you control
 * a battleground, this minion is strength +6. While you control 2 battlegrounds, this minion may not take wounds.
 */
public class Card4_179 extends AbstractMinion {
    public Card4_179() {
        super(7, 14, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Assault Band");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, Filters.race(Race.URUK_HAI));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self), new LocationCondition(Filters.keyword(Keyword.BATTLEGROUND)), Keyword.FIERCE, 1));
        modifiers.add(
                new StrengthModifier(self, Filters.sameCard(self), new SpotCondition(Filters.siteControlled(self.getOwner()), Filters.keyword(Keyword.BATTLEGROUND)), 6));
        modifiers.add(
                new CantTakeWoundsModifier(self, new SpotCondition(2, Filters.and(Filters.siteControlled(self.getOwner()), Filters.keyword(Keyword.BATTLEGROUND))), Filters.sameCard(self)));
        return modifiers;
    }
}
