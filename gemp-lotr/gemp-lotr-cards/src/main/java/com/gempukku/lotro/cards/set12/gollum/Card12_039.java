package com.gempukku.lotro.cards.set12.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: Each time the fellowship moves to a mountain or underground site, you may discard a [GOLLUM] event from
 * hand to take a minion from your discard pile into hand.
 */
public class Card12_039 extends AbstractPermanent {
    public Card12_039() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Not Alone");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, Filters.or(Keyword.MOUNTAIN, Keyword.UNDERGROUND))
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.GOLLUM, CardType.EVENT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.GOLLUM, CardType.EVENT));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
