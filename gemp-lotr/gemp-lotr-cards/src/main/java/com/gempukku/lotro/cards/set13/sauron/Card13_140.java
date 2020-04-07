package com.gempukku.lotro.cards.set13.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.discount.ExertCharactersDiscountEffect;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 16
 * Type: Minion • Maia
 * Strength: 24
 * Vitality: 5
 * Site: 6
 * Game Text: Damage +1. Fierce. When you play Sauron, you may exert any number of minions. For each minion you exert,
 * Sauron is twilight cost -X, where X is the current region number.
 */
public class Card13_140 extends AbstractMinion {
    public Card13_140() {
        super(16, 24, 5, 6, Race.MAIA, Culture.SAURON, "Sauron", "Dark Lord of Mordor", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        return Filters.countActive(game, CardType.MINION, Filters.canExert(self)) * GameUtils.getRegion(game);
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {
        action.appendPotentialDiscount(new ExertCharactersDiscountEffect(action, self, playerId, GameUtils.getRegion(game), CardType.MINION));
    }
}
