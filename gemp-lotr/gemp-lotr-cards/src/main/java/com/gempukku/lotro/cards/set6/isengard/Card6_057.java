package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 3
 * Site: 3
 * Game Text: This minion is strength +2 and damage +1 for each Free Peoples culture that you can spot over 2.
 */
public class Card6_057 extends AbstractMinion {
    public Card6_057() {
        super(5, 9, 3, 3, Race.MAN, Culture.ISENGARD, "Agents of Orthanc");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null, new CountCulturesEvaluator(2, 2, Side.FREE_PEOPLE)));
        modifiers.add(
                new KeywordModifier(self, self, null, Keyword.DAMAGE, new CountCulturesEvaluator(2, 1, Side.FREE_PEOPLE)));
        return modifiers;
    }
}
