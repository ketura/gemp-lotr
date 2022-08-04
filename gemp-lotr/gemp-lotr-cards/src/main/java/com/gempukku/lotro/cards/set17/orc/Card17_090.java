package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Possession • Armor
 * Strength: +2
 * Game Text: Bearer must be an [ORC] Orc. While bearer is mounted, it cannot take wounds except during
 * the skirmish phase.
 */
public class Card17_090 extends AbstractAttachable {
    public Card17_090() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.ORC, PossessionClass.ARMOR, "Rider's Gear");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ORC, Race.ORC);
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new CantTakeWoundsModifier(self, new NotCondition(new PhaseCondition(Phase.SKIRMISH)),
                        Filters.and(Filters.hasAttached(self), Filters.mounted)));
        return modifiers;
    }
}
