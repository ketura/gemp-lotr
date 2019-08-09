package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.actions.PlayPermanentAction;
import com.gempukku.lotro.logic.effects.DiscountEffect;
import com.gempukku.lotro.logic.effects.discount.DiscardCardFromHandDiscountEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Title: *Cave Troll of Moria, Monstrous Fiend
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 10
 * Type: Minion - Troll
 * Strength: 15
 * Vitality: 4
 * Home: 4
 * Card Number: 1R157
 * Game Text: Damage +1. Fierce.
 * When you play Cave Troll of Moria, you may discard X [MORIA] Goblins from hand; Cave Troll of Moria's twilight cost
 * is -1 for each Goblin discarded in this way.
 */
public class Card40_157 extends AbstractMinion {
    public Card40_157() {
        super(10, 15, 4, 4, Race.TROLL, Culture.MORIA, "Cave Troll of Moria", "Monstrous Fiend", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected int getPotentialExtraPaymentDiscount(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.filter(game.getGameState().getHand(playerId), game, Culture.MORIA, Race.GOBLIN).size();
    }

    @Override
    protected DiscountEffect getDiscountEffect(PlayPermanentAction action, String playerId, LotroGame game, PhysicalCard self) {
        return new DiscardCardFromHandDiscountEffect(action, playerId, Culture.MORIA, Race.GOBLIN);
    }
}
