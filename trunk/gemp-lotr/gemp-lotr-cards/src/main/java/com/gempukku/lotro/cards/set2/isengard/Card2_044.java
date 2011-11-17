package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.OpponentsCantLookOrRevealCardsFromHand;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot an [ISENGARD] minion. Plays to your support area. The Free Peoples player may not look at
 * or reveal cards in any Shadow player's hand.
 */
public class Card2_044 extends AbstractPermanent {
    public Card2_044() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "No Business of Ours");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new OpponentsCantLookOrRevealCardsFromHand(self));
    }
}
