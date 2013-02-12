package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 1
 * •Ham Gamgee, The Gaffer
 * Shire	Ally • Hobbit • Shire
 * 2	2
 * Each companion bearing a pipe is strength +1.
 */
public class Card20_390 extends AbstractAlly {
    public Card20_390() {
        super(1, null, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Ham Gamgee", "The Gaffer", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(CardType.COMPANION, Filters.hasAttached(PossessionClass.PIPE)), 1);
    }
}
