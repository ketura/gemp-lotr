package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.PreventableCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a companion is about to take a wound, exert a [GONDOR] companion to prevent that wound.
 */
public class Card2_034 extends AbstractResponseEvent {
    public Card2_034() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Gondor Will See It Done");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, CardType.COMPANION)
                && PlayConditions.canExert(self, game, Culture.GONDOR, CardType.COMPANION)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCards = woundEffect.getAffectedCardsMinusPrevented(game);
            final PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, CardType.COMPANION));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (PreventableCardEffect) effect, playerId, "Choose companion", CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
