package com.gempukku.lotro.cards.set13.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.cards.effects.discount.ExertCharactersDiscountEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

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
        super(16, 24, 5, 6, Race.MAIA, Culture.SAURON, "Sauron", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected int getPotentialExtraPaymentDiscount(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.MINION, Filters.canExert(self)) * GameUtils.getRegion(game.getGameState());
    }

    @Override
    protected DiscountEffect getDiscountEffect(PlayPermanentAction action, String playerId, LotroGame game, PhysicalCard self) {
        return new ExertCharactersDiscountEffect(action, self, playerId, GameUtils.getRegion(game.getGameState()), CardType.MINION);
    }
}
