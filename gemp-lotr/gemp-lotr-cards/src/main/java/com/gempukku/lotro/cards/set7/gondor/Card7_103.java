package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: At the start of your regroup phase, you may spot a [GONDOR] Man and discard a card from hand to remove
 * a threat.
 */
public class Card7_103 extends AbstractPermanent {
    public Card7_103() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Hearts Raised");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1));
            action.appendEffect(
                    new RemoveThreatsEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
