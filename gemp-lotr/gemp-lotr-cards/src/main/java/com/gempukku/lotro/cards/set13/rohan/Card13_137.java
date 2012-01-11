package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 7
 * Game Text: While you can spot Eowyn, Theoden is defender +1. While you can spot Eomer, Theoden is damage +1. While
 * you can spot Theodred, the move limit is +1.
 */
public class Card13_137 extends AbstractCompanion {
    public Card13_137() {
        super(3, 7, 3, 7, Culture.ROHAN, Race.MAN, null, "Theoden", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Filters.name("Eowyn")), Keyword.DEFENDER, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Filters.name("Eomer")), Keyword.DAMAGE, 1));
        modifiers.add(
                new MoveLimitModifier(self, new SpotCondition(Filters.name("Theodred")), 1));
        return modifiers;
    }
}
