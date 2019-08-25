package com.gempukku.lotro.cards.set1.gandalf;

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
import com.gempukku.lotro.logic.effects.PreventableCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Response: If a companion is about to take a wound, spot Gandalf to prevent that wound.
 */
public class Card1_076 extends AbstractResponseEvent {
    public Card1_076() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Intimidate");
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public List<PlayEventAction> getOptionalInHandBeforeActions(String playerId, LotroGame game, Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, CardType.COMPANION)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (PreventableCardEffect) effect, playerId, "Choose companion", CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
