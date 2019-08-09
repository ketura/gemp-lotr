package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotCultureTokensCondition;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 1
 * Site: 4
 * Game Text: Archer. While you can spot 4 or more culture tokens, this minion cannot take wounds except during
 * skirmishes.
 */
public class Card13_098 extends AbstractMinion {
    public Card13_098() {
        super(3, 8, 1, 4, Race.MAN, Culture.MEN, "Southron Murderer");
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public java.util.List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return java.util.Collections.singletonList(new CantTakeWoundsModifier(self,
new AndCondition(
new NotCondition(new PhaseCondition(Phase.SKIRMISH)),
new CanSpotCultureTokensCondition(4)), self));
}
}
