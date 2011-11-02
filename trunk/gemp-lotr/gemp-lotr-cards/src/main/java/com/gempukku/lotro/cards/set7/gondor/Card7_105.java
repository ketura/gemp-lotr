package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Bearer must be a [GONDOR] Man. If you have initiative, bearer is defender +1.
 */
public class Card7_105 extends AbstractAttachable {
    public Card7_105() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 3, Culture.GONDOR, null, "I Will Go", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), new InitiativeCondition(Side.FREE_PEOPLE), Keyword.DEFENDER, 1));
    }
}
