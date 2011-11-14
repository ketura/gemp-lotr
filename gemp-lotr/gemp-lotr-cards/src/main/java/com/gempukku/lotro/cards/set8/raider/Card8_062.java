package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotBurdensCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
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
 * Game Text: Easterling. While you can spot 4 burdens, Heavy Axeman is strength +4 and fierce.
 */
public class Card8_062 extends AbstractMinion {
    public Card8_062() {
        super(5, 11, 3, 4, Race.MAN, Culture.RAIDER, "Heavy Axeman", true);
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new SpotBurdensCondition(4), 4));
        modifiers.add(
                new KeywordModifier(self, self, new SpotBurdensCondition(4), Keyword.FIERCE, 1));
        return modifiers;
    }
}
