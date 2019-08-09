package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Black Steed, Bred to Serve
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 1
 * Type: Possession - Mount
 * Strength: +2
 * Card Number: 1R184
 * Game Text: Bearer must be a Nazgul. While skirmishing a companion with 3 or less resistance, bearer is strength +2.
 */
public class Card40_184 extends AbstractAttachable {
    public Card40_184() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.WRAITH, PossessionClass.MOUNT, "Black Steed", "Bred to Serve", false);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(CardType.COMPANION, Filters.maxResistance(3))), 2);
        return Collections.singletonList(modifier);
    }
}
