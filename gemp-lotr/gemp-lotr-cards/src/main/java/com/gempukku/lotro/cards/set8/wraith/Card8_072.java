package com.gempukku.lotro.cards.set8.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 7
 * Type: Minion • Orc
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: While you have initiative, each [WRAITH] minion is an archer.
 */
public class Card8_072 extends AbstractMinion {
    public Card8_072() {
        super(7, 13, 3, 4, Race.ORC, Culture.WRAITH, "Gothmog", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Culture.WRAITH, CardType.MINION), new InitiativeCondition(Side.SHADOW), Keyword.ARCHER, 1));
    }
}
