package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * Hobbit Sword
 * Shire	Possession • Hand Weapon
 * 2
 * Bearer must be a Hobbit.
 */
public class Card20_393 extends AbstractAttachableFPPossession {
    public Card20_393() {
        super(1, 2, 0, Culture.SHIRE, PossessionClass.HAND_WEAPON, "Hobbit Sword");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.HOBBIT;
    }
}
