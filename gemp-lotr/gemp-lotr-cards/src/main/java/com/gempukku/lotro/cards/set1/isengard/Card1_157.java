package com.gempukku.lotro.cards.set1.isengard;
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
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot an Uruk-hai, the fellowship archery total is -1.
 */
public class Card1_157 extends AbstractPermanent {
    public Card1_157() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, "Uruk-hai Armory");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.FREE_PEOPLE, new SpotCondition(Race.URUK_HAI), -1));
}
}
