package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.SpotCulturesCondition;
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
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 8
 * Game Text: Hunter 2. While you can spot 3 Free Peoples cultures, Aragorn is defender +1. While you can spot 2 Shadow
 * cultures, Aragorn is defender +1.
 */
public class Card18_038 extends AbstractCompanion {
    public Card18_038() {
        super(4, 8, 4, 8, Culture.GONDOR, Race.MAN, null, "Aragorn", "Heir to the Throne of Gondor", true);
        addKeyword(Keyword.HUNTER, 2);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self, new SpotCulturesCondition(3, Side.FREE_PEOPLE), Keyword.DEFENDER, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCulturesCondition(2, Side.SHADOW), Keyword.DEFENDER, 1));
        return modifiers;
    }
}
