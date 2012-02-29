package com.gempukku.lotro.cards.set17.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Plains. While you control this site, each of your hunter minions is strength +1.
 */
public class Card17_148 extends AbstractNewSite {
    public Card17_148() {
        super("Nurn", 1, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Modifier> getControlledSiteModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.owner(self.getCardController()), CardType.MINION, Keyword.HUNTER), 1));
    }
}
