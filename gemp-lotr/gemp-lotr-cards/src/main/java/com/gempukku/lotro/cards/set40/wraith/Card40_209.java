package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Ulaire Otsea, Shadowy Servent
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 4
 * Type: Minion - Nazgul
 * Strength: 9
 * Vitality: 2
 * Home: 3
 * Card Number: 1U209
 * Game Text: Each companion bearing a [RINGRWAITH] condition is resistance -2.
 */
public class Card40_209 extends AbstractMinion {
    public Card40_209() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, Names.otsea, "Shadowy Servent", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        ResistanceModifier modifier = new ResistanceModifier(self, Filters.and(CardType.COMPANION, Filters.hasAttached(Culture.WRAITH, CardType.CONDITION)), -2);
        return Collections.singletonList(modifier);
    }
}
