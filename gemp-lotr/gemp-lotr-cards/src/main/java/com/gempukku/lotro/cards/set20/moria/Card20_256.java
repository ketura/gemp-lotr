package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.discount.DiscardCardFromHandDiscountEffect;

/**
 * 10
 * •Cave Troll of Moria, Monstrous Fiend
 * Minion • Troll
 * 15	4	4
 * Damage +1. Fierce.
 * When you play Cave Troll of Moria, you may discard X [Moria] Goblins from hand; Cave Troll of Moria's twilight cost
 * is -1 for each [Moria] card discarded in this way.
 * http://www.lotrtcg.org/coreset/moria/cavetrollofmoriamf(r2).jpg
 */
public class Card20_256 extends AbstractMinion {
    public Card20_256() {
        super(10, 15, 4, 4, Race.TROLL, Culture.MORIA, Names.caveTroll, "Monstrous Fiend", true);
        addKeyword(Keyword.DAMAGE,  1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        return Filters.filter(game.getGameState().getHand(playerId), game, Culture.MORIA, Race.GOBLIN, Filters.not(self)).size();
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {
        action.appendPotentialDiscount(
                new DiscardCardFromHandDiscountEffect(action, playerId, Culture.MORIA, Race.GOBLIN));
    }
}
