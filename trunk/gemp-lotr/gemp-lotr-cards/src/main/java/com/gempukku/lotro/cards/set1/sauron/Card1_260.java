package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Search. Plays to your support area. While you can spot 7 companions, the move limit for this turn is -1
 * (to a minimum of 1).
 */
public class Card1_260 extends AbstractPermanent {
    public Card1_260() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "The Number Must Be Few");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new MoveLimitModifier(self, new SpotCondition(7, CardType.COMPANION), -1);
    }
}
