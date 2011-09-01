package com.gempukku.lotro.cards.set1.elven;

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
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Companion • Elf
 * Strength: 4
 * Vitality: 2
 * Resistance: 6
 * Game Text: To play, spot an Elf.
 */
public class Card1_053 extends AbstractCompanion {
    public Card1_053() {
        super(1, 5, 3, Culture.ELVEN, "Lorien Elf");
        addKeyword(Keyword.ELF);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF)))
            appendPlayCompanionActions(actions, game, self);

        appendHealCompanionActions(actions, game, self);

        return actions;
    }
}
