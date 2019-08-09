package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Treachery Runs Deep
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event - Response
 * Card Number: 1C143
 * Game Text: If a companion takes a wound, exert Saruman to wound that companion again.
 */
public class Card40_143 extends AbstractResponseEvent {
    public Card40_143() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Treachery Runs Deep");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachWounded(game, effectResult, self)
                && PlayConditions.canExert(self, game, Filters.saruman)
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            WoundResult woundResult = (WoundResult) effectResult;
            final PhysicalCard woundedCard = woundResult.getWoundedCard();
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.saruman));
            action.setText("Wound " + GameUtils.getFullName(woundedCard));
            action.appendEffect(
                    new WoundCharactersEffect(self, woundedCard));
            return Collections.singletonList(action);
        }
        return null;
    }
}
