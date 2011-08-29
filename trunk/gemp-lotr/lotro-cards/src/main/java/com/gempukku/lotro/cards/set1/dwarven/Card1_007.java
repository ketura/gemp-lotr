package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Companion � Dwarf
 * Strength: 4
 * Vitality: 2
 * Resistance: 6
 * Game Text: To play, spot a Dwarf.
 */
public class Card1_007 extends AbstractCompanion {
    public Card1_007() {
        super(1, 4, 2, Culture.DWARVEN, "Dwarf Guard", "1_7", false);
        addKeyword(Keyword.DWARF);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF))) {
            List<Action> actions = new LinkedList<Action>();

            appendPlayCompanionActions(actions, game, self);

            return actions;
        }
        return null;
    }
}
