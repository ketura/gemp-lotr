package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.cards.effects.discount.DiscardCardFromHandDiscountEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 10
 * •Cave Troll of Moria, Monstrous Fiend
 * Moria	Minion • Troll
 * 15	4	4
 * Damage +1. Fierce.
 * When you play Cave Troll of Moria, you may discard X [Moria] cards from hand; Cave Troll of Moria's twilight cost
 * is -1 for each [Moria] card discarded in this way.
 */
public class Card20_256 extends AbstractMinion {
    public Card20_256() {
        super(10, 15, 4, 4, Race.TROLL, Culture.MORIA, Names.caveTroll, "Monstrous Fiend", true);
        addKeyword(Keyword.DAMAGE,  1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected int getPotentialExtraPaymentDiscount(String playerId, LotroGame game, PhysicalCard self) {
        return -Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.MORIA, Filters.not(self)).size();
    }

    @Override
    protected DiscountEffect getDiscountEffect(PlayPermanentAction action, String playerId, LotroGame game, PhysicalCard self) {
        return new DiscardCardFromHandDiscountEffect(action, playerId, Culture.MORIA);
    }
}
