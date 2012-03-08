package com.gempukku.lotro.cards.set18.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.CantBeTransferredModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Treachery & Deceit
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Dwelling. While the fellowship is at this site, followers cannot be transfered to a character.
 */
public class Card18_140 extends AbstractNewSite {
    public Card18_140() {
        super("Streets of Bree", 0, Direction.LEFT);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantBeTransferredModifier(self, CardType.FOLLOWER);
    }
}
