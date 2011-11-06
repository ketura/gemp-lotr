package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
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
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: Ulaire Lemenya is strength +1 for each other [WRAITH] minion you spot. While you can spot 3 [WRAITH]
 * minions Ulaire Lemenya is fierce. While you can spot 4 [WRAITH] minions Ulaire Lemenya is damage +1.
 */
public class Card7_213 extends AbstractMinion {
    public Card7_213() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Lemenya", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null, new CountActiveEvaluator(Filters.not(self), Culture.WRAITH, CardType.MINION)));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(3, Culture.WRAITH, CardType.MINION), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(4, Culture.WRAITH, CardType.MINION), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
