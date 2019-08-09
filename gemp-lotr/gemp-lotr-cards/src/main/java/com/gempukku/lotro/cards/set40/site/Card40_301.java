package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: Eastern Ithilien
 * Set: Second Edition
 * Side: None
 * Site Number: 7
 * Shadow Number: 6
 * Card Number: 1U301
 * Game Text: Forest. While you can spot a Nazgul, each companion is resistance -1.
 */
public class Card40_301 extends AbstractSite {
    public Card40_301() {
        super("Eastern Ithilien", Block.SECOND_ED, 7, 6, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        ResistanceModifier modifier = new ResistanceModifier(self, CardType.COMPANION,
                new SpotCondition(Race.NAZGUL), -1);
        return Collections.singletonList(modifier);
    }
}
