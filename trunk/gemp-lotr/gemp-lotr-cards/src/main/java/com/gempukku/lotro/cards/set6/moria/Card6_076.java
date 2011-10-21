package com.gempukku.lotro.cards.set6.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.PreventMinionBeingAssignedToCharacterModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 14
 * Type: Minion • Balrog
 * Strength: 17
 * Vitality: 5
 * Site: 4
 * Game Text: Damage +2. The twilight cost of The Balrog is -3 if you can spot a [MORIA] minion. The Balrog cannot
 * be assigned to skirmish companions or allies of strength less than 6.
 */
public class Card6_076 extends AbstractMinion {
    public Card6_076() {
        super(14, 17, 5, 4, Race.BALROG, Culture.MORIA, "The Balrog", true);
        addKeyword(Keyword.DAMAGE, 2);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Culture.MORIA, CardType.MINION))
            return -3;
        return 0;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new PreventMinionBeingAssignedToCharacterModifier(self, null,
                        Filters.and(Filters.or(CardType.COMPANION, CardType.ALLY), Filters.lessStrengthThan(6)), self));
    }
}
