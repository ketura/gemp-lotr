package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.CancelEventEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Wizard's Grasp
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1C151
 * Game Text: Response: If a Free Peoples event is played, exert Saruman twice to cancel that event.
 */
public class Card40_151 extends AbstractPermanent {
    public Card40_151() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, "Wizard's Grasp");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Side.FREE_PEOPLE, CardType.EVENT)
                && PlayConditions.canExert(self, game, 2, Filters.saruman)) {
            PlayEventResult playEventResult = (PlayEventResult) effectResult;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.saruman));
            action.appendEffect(
                    new CancelEventEffect(self, playEventResult));
            return Collections.singletonList(action);
        }
        return null;
    }
}
