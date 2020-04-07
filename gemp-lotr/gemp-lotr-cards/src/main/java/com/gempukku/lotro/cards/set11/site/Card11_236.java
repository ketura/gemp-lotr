package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Forest. Each companion is twilight cost +2.
 */
public class Card11_236 extends AbstractShadowsSite {
    public Card11_236() {
        super("East Road", 0, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new TwilightCostModifier(self, CardType.COMPANION, 2));
}
}
