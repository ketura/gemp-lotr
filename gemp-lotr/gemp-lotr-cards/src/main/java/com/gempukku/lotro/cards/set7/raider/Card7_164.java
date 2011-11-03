package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotTwilightCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Archer. While you can spot 6 twilight tokens, other [RAIDER] Men are damage +1. While you can
 * spot 9 twilight tokens, other [RAIDER] Men are damage +1.
 */
public class Card7_164 extends AbstractMinion {
    public Card7_164() {
        super(5, 10, 3, 4, Race.MAN, Culture.RAIDER, "Southron Conqueror");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.not(self), Culture.RAIDER, Race.MAN), new CanSpotTwilightCondition(6), Keyword.DAMAGE, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.not(self), Culture.RAIDER, Race.MAN), new CanSpotTwilightCondition(9), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
