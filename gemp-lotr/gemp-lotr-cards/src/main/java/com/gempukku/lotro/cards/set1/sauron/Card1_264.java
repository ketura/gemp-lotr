package com.gempukku.lotro.cards.set1.sauron;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot a [SAURON] Orc, add 1 to the minion archery total.
 */
public class Card1_264 extends AbstractPermanent {
    public Card1_264() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.SAURON, "Orc Bowmen");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.SHADOW, new SpotCondition(Culture.SAURON, Race.ORC), 1));
}
}
