package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Rohirrim Commoner
 * Rohan	Ally • Man • Edoras
 * 4	2
 * Villager.
 * Fellowship: If you have Initiative, you may discard two cards from hand to play a [Rohan] fortification from your discard pile.
 */
public class Card20_338 extends AbstractAlly {
    public Card20_338() {
        super(1, null, 0, 4, 2, Race.MAN, Culture.ROHAN, "Rohirrim Commoner");
        addKeyword(Keyword.EDORAS);
        addKeyword(Keyword.VILLAGER);
    }

    @Override
    protected List<ActivateCardAction> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ROHAN, Keyword.FORTIFICATION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ROHAN, Keyword.FORTIFICATION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
