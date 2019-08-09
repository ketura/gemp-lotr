package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Orc Marskman
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion - Orc
 * Strength: 6
 * Vitality: 3
 * Home: 6
 * Card Number: 1C227
 * Game Text: Tracker. Archer. While you can spot 3 trackers, the minion archery total is +1.
 */
public class Card40_227 extends AbstractMinion {
    public Card40_227() {
        super(2, 6, 3, 6, Race.ORC, Culture.SAURON, "Orc Marksman", null, true);
        addKeyword(Keyword.TRACKER);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        ArcheryTotalModifier modifier = new ArcheryTotalModifier(self, Side.SHADOW,
                new SpotCondition(3, Keyword.TRACKER), 1);
        return Collections.singletonList(modifier);
    }
}
