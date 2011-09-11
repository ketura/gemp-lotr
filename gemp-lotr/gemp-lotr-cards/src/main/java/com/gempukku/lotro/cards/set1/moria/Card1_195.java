package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Remove (2) to play a [MORIA] possession from your discard pile.
 */
public class Card1_195 extends AbstractPermanent {
    public Card1_195() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, Zone.SHADOW_SUPPORT, "Relics of Moria");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 2)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SHADOW, "Remove (2) to play a [MORIA] possession from your discard pile.");
            action.addCost(new RemoveTwilightEffect(2));
            action.addEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, Filters.and(Filters.culture(Culture.MORIA), Filters.type(CardType.POSSESSION))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
