package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDiscardEffect;
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
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Discard 3 cards from hand to play a [MORIA] Orc from your discard
 * pile.
 */
public class Card1_196 extends AbstractPermanent {
    public Card1_196() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MORIA, Zone.SHADOW_SUPPORT, "They Are Coming");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && game.getGameState().getHand(playerId).size() >= 3) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Discard 3 cards from hand to play a [MORIA] Orc from your discard pile.");
            action.addCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, true));
            action.addCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, true));
            action.addCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, true));
            action.addEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, Filters.and(Filters.culture(Culture.MORIA), Filters.race(Race.ORC))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
