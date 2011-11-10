package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Minion • Orc
 * Strength: 5
 * Vitality: 2
 * Site: 6
 * Game Text: Tracker. The site number of each [SAURON] Orc is -1. While you have initiative, this minion is
 * strength +5.
 */
public class Card7_303 extends AbstractMinion {
    public Card7_303() {
        super(1, 5, 2, 6, Race.ORC, Culture.SAURON, "Orc Pursuer(2)");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MinionSiteNumberModifier(self, Filters.and(Culture.SAURON, Race.ORC), null, -1));
        modifiers.add(
                new StrengthModifier(self, self, new InitiativeCondition(Side.SHADOW), 5));
        return modifiers;
    }
}
