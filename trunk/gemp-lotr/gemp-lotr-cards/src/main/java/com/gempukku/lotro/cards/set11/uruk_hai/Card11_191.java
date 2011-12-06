package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Possession • Ranged Weapon
 * Vitality: +1
 * Game Text: Bearer must be an [URUK-HAI] minion. While bearer is at a battleground site, it is an archer.
 */
public class Card11_191 extends AbstractAttachable {
    public Card11_191() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.URUK_HAI, PossessionClass.RANGED_WEAPON, "Isengard Siege Bow");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.URUK_HAI, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new VitalityModifier(self, self, 1));
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), Keyword.ARCHER, 1));
        return modifiers;
    }
}
