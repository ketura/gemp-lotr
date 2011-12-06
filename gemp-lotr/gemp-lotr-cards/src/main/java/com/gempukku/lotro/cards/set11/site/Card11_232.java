package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.PlayersCantUsePhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Underground. Skirmish special abilities cannot be used.
 */
public class Card11_232 extends AbstractNewSite {
    public Card11_232() {
        super("Cavern Entrance", 1, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new PlayersCantUsePhaseSpecialAbilitiesModifier(self, Phase.SKIRMISH);
    }
}
