package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.discount.OptionalDiscardDiscountEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 10
 * Type: Minion • Troll
 * Strength: 22
 * Vitality: 6
 * Site: 5
 * Game Text: When you play this minion, you may discard 5 [ORC] minions from play to make it twilight cost -10
 * and fierce. Shadow: Remove (3) to play an [ORC] Orc from your discard pile. Its twilight cost is -2.
 */
public class Card15_112 extends AbstractMinion {
    public Card15_112() {
        super(10, 22, 6, 5, Race.TROLL, Culture.ORC, "Mountain-troll");
    }

    @Override
    protected int getPotentialExtraPaymentDiscount(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canDiscardFromPlay(self, game, 5, Culture.ORC, CardType.MINION))
            return 10;
        return 0;
    }

    @Override
    protected DiscountEffect getDiscountEffect(final PlayPermanentAction action, String playerId, LotroGame game, final PhysicalCard self) {
        return new OptionalDiscardDiscountEffect(action, 10, playerId, 5, Culture.ORC, CardType.MINION) {
            @Override
            protected void discountPaidCallback() {
                action.appendEffect(
                        new AddUntilEndOfTurnModifierEffect(
                                new KeywordModifier(self, self, Keyword.FIERCE)));
            }
        };
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 3)
                && PlayConditions.canPlayFromDiscard(playerId, game, 3, -2, Culture.ORC, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Culture.ORC, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }
}
