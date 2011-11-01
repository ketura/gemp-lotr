package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Stealth. Bearer must be Smeagol. If Smeagol is about to be killed in a skirmish, he is discarded instead.
 */
public class Card7_056 extends AbstractAttachable {
    public Card7_056() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 3, Culture.GOLLUM, null, "The Dead City");
        addKeyword(Keyword.STEALTH);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.smeagol;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.isGettingKilled(effect, game, Filters.hasAttached(self))
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
