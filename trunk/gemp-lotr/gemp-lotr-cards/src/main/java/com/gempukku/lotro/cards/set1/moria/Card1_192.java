package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot a [MORIA] archer, the fellowship archery total is -1.
 */
public class Card1_192 extends AbstractPermanent {
    public Card1_192() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Pinned Down");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.FREE_PEOPLE, new SpotCondition(Culture.MORIA, Keyword.ARCHER), -1);
    }
}
