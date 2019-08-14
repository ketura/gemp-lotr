package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.CancelEventEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Response: If a stealth event is played, spot 3 [MORIA] minions to cancel that event.
 */
public class Card1_194 extends AbstractResponseEvent {
    public Card1_194() {
        super(Side.SHADOW, 0, Culture.MORIA, "Relentless");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.EVENT, Keyword.STEALTH)
                && PlayConditions.canSpot(game, 3, Culture.MORIA, CardType.MINION)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(new CancelEventEffect(self, (PlayEventResult) effectResult));
            return Collections.singletonList(action);
        }
        return null;
    }
}
