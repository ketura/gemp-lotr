package com.gempukku.lotro.cards.set11.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Twilight Cost: 3
 * Type: Site
 * Game Text: River. The minion archery total is -3.
 */
public class Card11_250 extends AbstractShadowsSite {
    public Card11_250() {
        super("North Undeep", 3, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.SHADOW, -3));
}
}
