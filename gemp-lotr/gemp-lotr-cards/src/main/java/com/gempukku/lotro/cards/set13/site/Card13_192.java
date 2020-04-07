package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Battleground. While a player can spot more companions than minions, each minion is strength +1.
 */
public class Card13_192 extends AbstractShadowsSite {
    public Card13_192() {
        super("The Great Gates", 0, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, CardType.MINION,
                new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return Filters.countActive(game, CardType.COMPANION)
                                > Filters.countActive(game, CardType.MINION);
                    }
                }, 1));
    }
}
