package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Possession • Ranged Weapon
 * Game Text: Bearer must be a [RAIDER] Man. Bearer is an archer and ambush (5).
 */
public class Card7_155 extends AbstractAttachable {
    public Card7_155() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.RAIDER, PossessionClass.RANGED_WEAPON, "Raider Bow");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.RAIDER, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.AMBUSH, 5));
        return modifiers;
    }
}
