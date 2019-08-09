package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Southern Star, Southfarthing Vintage
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession - Support Area
 * Card Number: 1C271
 * Game Text: Pipeweed. When you play this possession, you may discard a card from hand to heal a Hobbit.
 */
public class Card40_271 extends AbstractPermanent {
    public Card40_271() {
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.SHIRE, "Southern Star",
                "Southfarthing Vintage", false);
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
            && PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Filters.any));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
