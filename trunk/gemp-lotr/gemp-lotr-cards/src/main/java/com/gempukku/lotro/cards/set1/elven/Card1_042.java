package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
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
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession � Cloak
 * Game Text: To play, spot an Elf. Bearer must be a companion. The minion archery total is -1.
 */
public class Card1_042 extends AbstractAttachableFPPossession {
    public Card1_042() {
        super(1, Culture.ELVEN, "Elven Cloak", "1_42");
        addKeyword(Keyword.CLOAK);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.type(CardType.COMPANION), Filters.not(Filters.attached(Filters.keyword(Keyword.CLOAK))));

        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF)))
            appendAttachCardAction(actions, game, self, validTargetFilter);

        appendTransferPossessionAction(actions, game, self, validTargetFilter);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.SHADOW, -1);
    }
}
