package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: While the fellowship is at a battleground or underground site, this minion cannot take wounds in
 * the archery phase.
 */
public class Card18_088 extends AbstractMinion {
    public Card18_088() {
        super(3, 8, 2, 4, Race.ORC, Culture.ORC, "Orkish Defender");
    }

    @Override
    public java.util.List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return java.util.Collections.singletonList(new CantTakeWoundsModifier(self,
new AndCondition(
new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.UNDERGROUND)),
new PhaseCondition(Phase.ARCHERY)), self));
}
}
