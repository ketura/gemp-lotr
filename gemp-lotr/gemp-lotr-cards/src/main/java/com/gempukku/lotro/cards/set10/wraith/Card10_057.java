package com.gempukku.lotro.cards.set10.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CancelKeywordBonusModifier;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Each companion skirmishing this minion loses all damage and strength bonuses from possessions.
 */
public class Card10_057 extends AbstractMinion {
    public Card10_057() {
        super(4, 9, 2, 4, Race.ORC, Culture.WRAITH, "Cirith Ungol Watchman");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CancelStrengthBonusModifier(self, Filters.and(CardType.POSSESSION, Filters.attachedTo(CardType.COMPANION, Filters.inSkirmishAgainst(self)))));
        modifiers.add(
                new CancelKeywordBonusModifier(self, Keyword.DAMAGE, Filters.and(CardType.POSSESSION, Filters.attachedTo(CardType.COMPANION, Filters.inSkirmishAgainst(self)))));
        return modifiers;
    }
}
