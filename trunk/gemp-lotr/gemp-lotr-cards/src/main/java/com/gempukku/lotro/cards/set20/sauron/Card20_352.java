package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotFPCulturesCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Blade of Morannon
 * Sauron	Possession • Hand weapon
 * 2
 * Bearer must be a [Sauron] Orc.
 * While you cannot spot 3 Free Peoples cultures, bearer is fierce.
 */
public class Card20_352 extends AbstractAttachable {
    public Card20_352() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.SAURON, PossessionClass.HAND_WEAPON, "Blade of Morannon");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.SAURON, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self),
                        new NotCondition(new CanSpotFPCulturesCondition(self.getOwner(), 3)), Keyword.FIERCE, 1));
        return modifiers;
    }
}
