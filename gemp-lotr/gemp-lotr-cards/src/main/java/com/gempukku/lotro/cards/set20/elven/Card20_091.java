package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Long Knives of Legolas, Dancing Blades
 * Elven	Possession  Hand Weapon
 * 1
 * Bearer must be Legolas.
 * Each wounded minion skirmishing legolas is strength -2.
 */
public class Card20_091 extends AbstractAttachableFPPossession {
    public Card20_091() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Long Knives of Legolas", "Dancing Blades", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.legolas;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(CardType.MINION, Filters.wounded, Filters.inSkirmishAgainst(Filters.legolas)), -2));
    }
}
