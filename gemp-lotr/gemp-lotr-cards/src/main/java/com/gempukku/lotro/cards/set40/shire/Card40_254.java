package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Title: Hobbit Sword
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession - Hand Weapon
 * Strength: +2
 * Card Number: 1C254
 * Game Text: Bearer must be a Hobbit.
 */
public class Card40_254 extends AbstractAttachableFPPossession {
    public Card40_254() {
        super(1, 2, 0, Culture.SHIRE, PossessionClass.HAND_WEAPON, "Hobbit Sword");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.HOBBIT;
    }
}
