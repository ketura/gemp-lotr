package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Shield
 * Game Text: Bearer must be a [GONDOR] companion. The minion archery total is -1. If bearer is Boromir, each minion
 * skirmishing him does not gain strength bonuses from weapons.
 */
public class Card2_038 extends AbstractAttachableFPPossession {
    public Card2_038() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.SHIELD, "Shield of Boromir", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new ArcheryTotalModifier(self, Side.SHADOW, -1));
        modifiers.add(
                new CancelStrengthBonusTargetModifier(self,
                        new SpotCondition(Filters.boromir, Filters.hasAttached(self)),
                        Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.boromir)),
                        Filters.weapon));
        return modifiers;
    }
}
