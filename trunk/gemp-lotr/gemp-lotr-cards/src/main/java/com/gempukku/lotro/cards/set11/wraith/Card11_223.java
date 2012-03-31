package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: Lurker. (Skirmishes involving lurker minions must be resolved after any others.) While Úlairë Nertëa is
 * at a forest site, he is strength +2. While you can spot 6 companions, each Nazgul is strength +2.
 */
public class Card11_223 extends AbstractMinion {
    public Card11_223() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Nertëa", "Ninth of the Nine Riders", true);
        addKeyword(Keyword.LURKER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Keyword.FOREST), 2));
        modifiers.add(
                new StrengthModifier(self, Race.NAZGUL, new SpotCondition(6, CardType.COMPANION), 2));
        return modifiers;
    }
}
