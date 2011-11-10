package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.modifiers.OpponentsCantLookOrRevealCardsFromHand;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Ally • Home 3 • Man
 * Strength: 5
 * Vitality: 2
 * Site: 3
 * Game Text: To play, spot Gandalf. Shadow players may not look at or reveal cards in your hand.
 */
public class Card2_021 extends AbstractAlly {
    public Card2_021() {
        super(2, Block.FELLOWSHIP, 3, 5, 2, Race.MAN, Culture.GANDALF, "Erland", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.gandalf);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new OpponentsCantLookOrRevealCardsFromHand(self, self.getOwner());
    }
}
