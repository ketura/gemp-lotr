package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot a [SAURON] Orc, add 1 to the minion archery total.
 */
public class Card1_264 extends AbstractPermanent {
    public Card1_264() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Orc Bowmen");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.SHADOW, new SpotCondition(Culture.SAURON, Race.ORC), 1);
    }
}
