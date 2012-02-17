package com.gempukku.lotro.cards.set12.men;

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
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: This minion is strength +1 for each other character you can spot. While you can spot a companion who has
 * resistance 3 or less, this minion is damage +1. While you can spot a companion who has resistance 0, this minion
 * is fierce.
 */
public class Card12_057 extends AbstractMinion {
    public Card12_057() {
        super(4, 6, 2, 4, Race.MAN, Culture.MEN, "Corrupted Spy");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null, new CountActiveEvaluator(Filters.not(self), Filters.character)));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(CardType.COMPANION, Filters.maxResistance(3)), Keyword.DAMAGE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(CardType.COMPANION, Filters.maxResistance(0)), Keyword.FIERCE, 1));
        return modifiers;
    }
}
