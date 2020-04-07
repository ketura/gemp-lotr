package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountCulturesEvaluator;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Possession • Hand weapon
 * Strength: +1
 * Game Text: Bearer must be an [ORC] Orc. Bearer gains hunter X (While skirmishing a non-hunter character,
 * this character is strength +X.), where X is the number of Free Peoples cultures you can spot.
 */
public class Card15_114 extends AbstractAttachable {
    public Card15_114() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.ORC, PossessionClass.HAND_WEAPON, "Orkish Hunting Spear");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ORC, Race.ORC);
    }

    @Override
    public int getStrength() {
        return 1;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), null, Keyword.HUNTER, new CountCulturesEvaluator(Side.FREE_PEOPLE)));
        return modifiers;
    }
}
