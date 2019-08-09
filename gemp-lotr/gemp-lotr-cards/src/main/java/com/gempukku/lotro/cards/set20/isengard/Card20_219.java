package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * 3
 * Uruk Trooper
 * Isengard	Minion • Uruk-hai
 * 7	2	5
 * Damage +1.
 * This minion is strength + 1 for each battleground in the current region.
 */
public class Card20_219 extends AbstractMinion {
    public Card20_219() {
        super(3, 7, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Trooper");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        return Filters.countActive(gameState, modifiersQuerying, CardType.SITE, Keyword.BATTLEGROUND, Filters.region(GameUtils.getRegion(gameState)));
                    }
                });
    }
}
