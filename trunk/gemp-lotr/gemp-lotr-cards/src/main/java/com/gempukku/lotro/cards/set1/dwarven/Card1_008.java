package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Possession � Armor
 * Game Text: Bearer must be a Dwarf. Bearer may not be overwhelmed unless his strength is tripled.
 */
public class Card1_008 extends AbstractAttachableFPPossession {
    public Card1_008() {
        super(0, Culture.DWARVEN, "Dwarven Armor", "1_8");
        addKeyword(Keyword.ARMOR);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.keyword(Keyword.DWARF), Filters.not(Filters.attached(Filters.keyword(Keyword.ARMOR))));

        appendAttachCardFromHandAction(actions, game, self, validTargetFilter);
        appendTransferPossessionAction(actions, game, self, validTargetFilter);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new OverwhelmedByMultiplierModifier(self, Filters.attachedTo(self), 3);
    }
}
