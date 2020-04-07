package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.SpecialFlagModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Underground. The Free Peoples player may transfer Free Peoples artifacts and possessions at no twilight
 * cost.
 */
public class Card11_233 extends AbstractShadowsSite {
    public Card11_233() {
        super("Chamber of Mazarbul", 2, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new SpecialFlagModifier(self, ModifierFlag.TRANSFERS_FOR_FREE));
}
}
