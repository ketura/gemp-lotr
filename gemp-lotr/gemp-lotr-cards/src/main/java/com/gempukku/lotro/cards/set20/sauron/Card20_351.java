package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotFPCulturesCondition;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Blade of Gorgoroth
 * Possession • Hand weapon
 * 2
 * Bearer must be a [Sauron] Orc.
 * While you cannot spot 3 Free Peoples cultures, bearer is strength +2.
 * http://lotrtcg.org/coreset/sauron/bladeofgorgoroth(r1).png
 */
public class Card20_351 extends AbstractAttachable {
    public Card20_351() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.SAURON, PossessionClass.HAND_WEAPON, "Blade of Gorgoroth");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.SAURON, Race.ORC);
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self),
                        new NotCondition(new CanSpotFPCulturesCondition(self.getOwner(), 3)), 2));
        return modifiers;
    }
}
