package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be a [MEN] minion. Bearer may bear this hand weapon in addition to 1 other hand weapon.
 */
public class Card18_069 extends AbstractAttachable {
    public Card18_069() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.MEN, PossessionClass.HAND_WEAPON, "Henchman's Dagger");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, CardType.MINION);
    }

    @Override
    public int getStrength() {
        return 1;
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return true;
    }
}
