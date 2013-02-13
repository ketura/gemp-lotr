package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Buckleberry Ferry
 * 2	1
 * River.
 * While only Hobbits are in the fellowship, there are no assignment and skirmish phases at Buckleberry Ferry.
 */
public class Card20_423 extends AbstractSite {
    public Card20_423() {
        super("Buckleberry Ferry", Block.SECOND_ED, 2, 1, null);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        Condition condition = new NotCondition(new GameHasCondition(CardType.COMPANION, Filters.not(Race.HOBBIT)));
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ShouldSkipPhaseModifier(self, condition, Phase.ASSIGNMENT));
        modifiers.add(
                new ShouldSkipPhaseModifier(self, condition, Phase.SKIRMISH));
        return modifiers;
    }
}
