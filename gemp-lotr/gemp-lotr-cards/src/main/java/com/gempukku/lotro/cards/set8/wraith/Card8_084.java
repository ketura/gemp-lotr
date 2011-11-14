package com.gempukku.lotro.cards.set8.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Enduring. Each Man skirmishing The Witch-king loses all strength bonuses from weapons.
 */
public class Card8_084 extends AbstractMinion {
    public Card8_084() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, "The Witch-king", true);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CancelStrengthBonusModifier(self,
                        Filters.and(Filters.weapon, Filters.attachedTo(Race.MAN, Filters.inSkirmishAgainst(self)))));
    }
}
