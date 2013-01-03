package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotCultureTokensCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
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
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self,
                new AndCondition(
                        new NotCondition(new PhaseCondition(Phase.SKIRMISH)),
                        new CanSpotCultureTokensCondition(4)), self);
    }
}
