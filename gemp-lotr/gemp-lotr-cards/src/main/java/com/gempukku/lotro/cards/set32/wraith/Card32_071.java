package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. While you can spot a follower attached to a companion, Ulaire Nelya is damage +1.
 */
public class Card32_071 extends AbstractMinion {
    public Card32_071() {
        super(5, 10, 3, 5, Race.NAZGUL, Culture.WRAITH, Names.nelya, "Revived", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, self, new SpotCondition(CardType.FOLLOWER, Filters.attachedTo(CardType.COMPANION)), Keyword.DAMAGE, 1));
    }
}
