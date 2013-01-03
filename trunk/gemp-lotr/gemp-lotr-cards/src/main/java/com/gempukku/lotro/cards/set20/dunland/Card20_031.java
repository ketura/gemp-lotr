package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
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
 * 1
 * Hillman Axe
 * Dunland	Possession • Hand Weapon
 * +2
 * Bearer must be a [Dunland] Man.
 * While you control a site, bearer is damage +1.
 */
public class Card20_031 extends AbstractAttachable {
    public Card20_031() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.DUNLAND, PossessionClass.HAND_WEAPON, "Hillman Axe");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.DUNLAND, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new SpotCondition(Filters.siteControlled(self.getOwner())), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
