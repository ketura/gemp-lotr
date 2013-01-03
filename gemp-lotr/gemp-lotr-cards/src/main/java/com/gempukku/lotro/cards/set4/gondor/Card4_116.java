package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Ring-bound. Ranger. The twilight cost of each other [GONDOR] Man in your starting fellowship is -1.
 * While skirmishing a roaming minion, Faramir is strength +2.
 */
public class Card4_116 extends AbstractCompanion {
    public Card4_116() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, Signet.ARAGORN, "Faramir", "Captain of Gondor", true);
        addKeyword(Keyword.RING_BOUND);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new TwilightCostModifier(self, Filters.and(Filters.not(self), Culture.GONDOR, Race.MAN),
                        new PhaseCondition(Phase.PLAY_STARTING_FELLOWSHIP), -1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(self, Filters.inSkirmishAgainst(Filters.and(CardType.MINION, Keyword.ROAMING))), 2));
        return modifiers;
    }
}
