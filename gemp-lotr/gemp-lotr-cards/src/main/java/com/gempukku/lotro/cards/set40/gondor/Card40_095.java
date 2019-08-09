package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.timing.ExtraFilters;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.AttachPermanentAction;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Aragorn, Isildur's Heir
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion - Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1R95
 * Game Text: Ranger. Maneuver: Play an artifact or possession on Aragorn to heal him.
 */
public class Card40_095 extends AbstractCompanion {
    public Card40_095() {
        super(4, 8, 4, 8, Culture.GONDOR, Race.MAN, null, "Aragorn", "Isildur's Heir", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canPlayFromHand(playerId, game, Filters.or(CardType.ARTIFACT, CardType.POSSESSION), ExtraFilters.attachableTo(game, self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseCardsFromHandEffect(playerId, 1, 1, Filters.or(CardType.ARTIFACT, CardType.POSSESSION), ExtraFilters.attachableTo(game, self)) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                AttachPermanentAction attachPermanentAction = ((AbstractAttachable) card.getBlueprint()).getPlayCardAction(playerId, game, card, Filters.and(self), 0);
                                game.getActionsEnvironment().addActionToStack(attachPermanentAction);
                            }
                        }
                    });
            action.appendEffect(
                    new HealCharactersEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
