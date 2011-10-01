package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.CancelKeywordBonusModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each character skirmishing an [ISENGARD] Orc loses all damage bonuses
 * from weapons.
 */
public class Card3_072 extends AbstractPermanent {
    public Card3_072() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, Zone.SHADOW_SUPPORT, "Trapped and Alone");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CancelKeywordBonusModifier(self, Keyword.DAMAGE,
                        Filters.and(
                                Filters.weapon(),
                                Filters.attachedTo(Filters.inSkirmishAgainst(Filters.and(Filters.culture(Culture.ISENGARD), Filters.race(Race.ORC))))
                        )));
    }
}
