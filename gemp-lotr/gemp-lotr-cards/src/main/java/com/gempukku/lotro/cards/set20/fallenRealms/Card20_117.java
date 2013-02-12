package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 3
 * Easterling Recruit
 * Fallen Realms	Minion • Man
 * 6	2	4
 * Easterling. Toil 1.
 * This minion is strength +1 for each wounded minion.
 */
public class Card20_117 extends AbstractMinion {
    public Card20_117() {
        super(3, 6, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Recruit");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountActiveEvaluator(CardType.MINION, Filters.wounded));
    }
}
