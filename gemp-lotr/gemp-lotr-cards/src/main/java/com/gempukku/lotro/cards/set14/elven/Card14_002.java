package com.gempukku.lotro.cards.set14.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Companion • Elf
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Elrohir is twilight cost -2. Each time you play an [ELVEN] event during a skirmish involving Elladan
 * or Elrohir, that event is twilight cost -2.
 */
public class Card14_002 extends AbstractCompanion {
    public Card14_002() {
        super(3, 7, 3, 6, Culture.ELVEN, Race.ELF, null, "Elladan", "Son of Elrond", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new TwilightCostModifier(self, Filters.name("Elrohir"), -2));
        modifiers.add(
                new TwilightCostModifier(self,
                        Filters.and(Filters.owner(self.getOwner()), CardType.EVENT, Culture.ELVEN),
                        new SpotCondition(Filters.or(Filters.name("Elladan"), Filters.name("Elrohir")), Filters.inSkirmish), -2));
        return modifiers;
    }
}
