package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Stalker
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion - Orc
 * Strength: 10
 * Vitality: 3
 * Home: 6
 * Card Number: 1C236
 * Game Text: Tracker. While you can spot 3 trackers, this minion is fierce.
 */
public class Card40_236 extends AbstractMinion {
    public Card40_236() {
        super(4, 10, 3, 6, Race.ORC, Culture.SAURON, "Orc Stalker");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, self,
                new SpotCondition(3, Keyword.TRACKER), Keyword.FIERCE, 1);
        return Collections.singletonList(modifier);
    }
}
