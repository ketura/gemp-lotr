package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CantTakeArcheryWoundsModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Archer. While you can spot another [RAIDER] Man, Ring-bound companions cannot take archery
 * wounds.
 */
public class Card8_060 extends AbstractMinion {
    public Card8_060() {
        super(5, 11, 3, 4, Race.MAN, Culture.RAIDER, "Haradrim Marksman", true);
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeArcheryWoundsModifier(self,
                        new SpotCondition(Filters.not(self), Culture.RAIDER, Race.MAN),
                        Filters.and(CardType.COMPANION, Keyword.RING_BOUND)));
    }
}
