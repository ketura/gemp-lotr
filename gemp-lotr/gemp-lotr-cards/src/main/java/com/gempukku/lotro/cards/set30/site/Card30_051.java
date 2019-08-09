package com.gempukku.lotro.cards.set30.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Main Deck
 * Twilight Cost: 1
 * Type: Site
 * Site: 3
 * Game Text: Sanctuary. Trolls cannot be played at Rivendell.
 */
public class Card30_051 extends AbstractSite {
    public Card30_051() {
        super("Rivendell", SitesBlock.HOBBIT, 3, 1, Direction.RIGHT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new CantPlayCardsModifier(self, Race.TROLL);
    }
}