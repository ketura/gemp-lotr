package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 11
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. While this minion is at a battleground site, it cannot take wounds. While you can spot
 * 6 companions, each [URUK-HAI] minion cannot take wounds.
 */
public class Card11_190 extends AbstractMinion {
    public Card11_190() {
        super(4, 11, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Invincible Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantTakeWoundsModifier(self, new LocationCondition(Keyword.BATTLEGROUND), self));
        modifiers.add(
                new CantTakeWoundsModifier(self, new SpotCondition(6, CardType.COMPANION), Filters.and(Culture.URUK_HAI, CardType.MINION)));
        return modifiers;
    }
}
