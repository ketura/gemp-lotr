package com.gempukku.lotro.cards.set11.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.PlayersCantUsePhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Underground. Skirmish special abilities cannot be used.
 */
public class Card11_232 extends AbstractShadowsSite {
    public Card11_232() {
        super("Cavern Entrance", 1, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new PlayersCantUsePhaseSpecialAbilitiesModifier(self, Phase.SKIRMISH));
}
}
