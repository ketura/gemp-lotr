package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Rohirrim Villager
 * Rohan	Ally • Man • Edoras
 * 4	2
 * Villager.
 * Fellowship: If you have initiative, discard 2 cards from hand to heal a [Rohan] Man.
 */
public class Card20_342 extends AbstractAlly {
    public Card20_342() {
        super(1, null, 0, 4, 2, Race.MAN, Culture.ROHAN, "Rohirrim Villager");
        addKeyword(Keyword.VILLAGER);
        addKeyword(Keyword.EDORAS);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2, Filters.any));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Culture.ROHAN, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
