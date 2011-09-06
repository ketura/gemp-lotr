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
        super(1, Culture.ELVEN, "Elven Cloak");
        addKeyword(Keyword.CLOAK);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.type(CardType.COMPANION), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.CLOAK))));
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.SHADOW, -1);
    }
}
