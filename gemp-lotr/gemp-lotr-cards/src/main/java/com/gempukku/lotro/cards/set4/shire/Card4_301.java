package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.CantDiscardCardsFromHandOrTopOfDeckModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Frodo
 * Game Text: Ring-bearer (resistance 10). While you can spot 3 unbound companions, Shadow cards may not discard cards
 * from your hand or from the top of your draw deck.
 */
public class Card4_301 extends AbstractCompanion {
    public Card4_301() {
        super(0, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Frodo", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public int getResistance() {
        return 10;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantDiscardCardsFromHandOrTopOfDeckModifier(self, new SpotCondition(3, Filters.unboundCompanion), self.getOwner(), Side.SHADOW));
    }
}
