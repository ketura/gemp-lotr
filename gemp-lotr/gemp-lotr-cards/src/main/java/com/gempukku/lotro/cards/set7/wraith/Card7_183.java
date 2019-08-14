package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event • Response
 * Game Text: If a Nazgul kills a character, wound each character bearing a [WRAITH] condition.
 */
public class Card7_183 extends AbstractResponseEvent {
    public Card7_183() {
        super(Side.SHADOW, 1, Culture.WRAITH, "Mind and Body");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilledBy(game, effectResult, Race.NAZGUL, Filters.or(CardType.COMPANION, CardType.ALLY))
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.or(CardType.COMPANION, CardType.ALLY, CardType.MINION), Filters.hasAttached(Culture.WRAITH, CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
