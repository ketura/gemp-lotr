package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Rohirrim Peasant
 * Rohan	Ally • Man • Edoras
 * 4	2
 * Villager.
 * Skirmish: If you have initiaitve, discard a card from hand to make a [rohan] man strength +1.
 */
public class Card20_339 extends AbstractAlly {
    public Card20_339() {
        super(1, null, 0, 4, 2, Race.MAN, Culture.ROHAN, "Rohirrim Peasant");
        addKeyword(Keyword.VILLAGER);
        addKeyword(Keyword.EDORAS);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Filters.any));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Culture.ROHAN, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
