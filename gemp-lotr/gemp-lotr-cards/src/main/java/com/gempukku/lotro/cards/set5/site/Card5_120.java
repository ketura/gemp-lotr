package com.gempukku.lotro.cards.set5.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Twilight Cost: 9
 * Type: Site
 * Site: 9T
 * Game Text: Underground. The Free Peoples player wins the game only if the Ring-bearer survives until Shadow players
 * reconcile.
 */
public class Card5_120 extends AbstractSite {
    public Card5_120() {
        super("Caverns of Isengard", Block.TWO_TOWERS, 9, 9, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new SpecialFlagModifier(self, ModifierFlag.WIN_CHECK_AFTER_SHADOW_RECONCILE));
    }
}
