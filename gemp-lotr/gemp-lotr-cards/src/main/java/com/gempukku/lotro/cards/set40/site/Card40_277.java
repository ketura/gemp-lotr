package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: The Prancing Pony
 * Set: Second Edition
 * Side: None
 * Site Number: 1
 * Shadow Number: 0
 * Card Number: 1U277
 * Game Text: Fellowship: Add a burden to play Aragorn from your draw deck.
 */
public class Card40_277 extends AbstractSite {
    public Card40_277() {
        super("The Prancing Pony", SitesBlock.SECOND_ED, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canAddBurdens(game, playerId, self)
                && PlayConditions.canPlayFromDeck(playerId, game, Filters.aragorn)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.aragorn));
            return Collections.singletonList(action);
        }
        return null;
    }
}
