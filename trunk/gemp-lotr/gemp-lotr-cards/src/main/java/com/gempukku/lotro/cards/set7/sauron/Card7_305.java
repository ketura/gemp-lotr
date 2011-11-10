package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: While you can spot 6 companions and another [SAURON] Orc, this minion is strength +9 and fierce.
 */
public class Card7_305 extends AbstractMinion {
    public Card7_305() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Savage");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self,
                        new AndCondition(
                                new SpotCondition(6, CardType.COMPANION),
                                new SpotCondition(Filters.not(self), Culture.SAURON, Race.ORC)
                        ), 9));
        modifiers.add(
                new KeywordModifier(self, self,
                        new AndCondition(
                                new SpotCondition(6, CardType.COMPANION),
                                new SpotCondition(Filters.not(self), Culture.SAURON, Race.ORC)
                        ), Keyword.FIERCE, 1));
        return modifiers;
    }
}
