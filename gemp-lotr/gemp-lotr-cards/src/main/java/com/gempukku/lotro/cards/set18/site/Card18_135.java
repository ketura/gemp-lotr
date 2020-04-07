package com.gempukku.lotro.cards.set18.site;

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
 * Set: Treachery & Deceit
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Mountain. Each card that is about to be placed into the discard pile is removed from the game instead.
 */
public class Card18_135 extends AbstractShadowsSite {
    public Card18_135() {
        super("Foot of Mount Doom", 2, Direction.LEFT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new SpecialFlagModifier(self, ModifierFlag.REMOVE_CARDS_GOING_TO_DISCARD));
}
}
